pub mod advanced_table;
mod anvil;
mod combat;
mod commands;
mod config;
mod enchants;
pub mod enchlib_bridge;
pub mod spell_books;
mod survival;

use mod_api::{
    AnvilUpdateEvent, AttackEntityEvent, BlockDropsEvent, EnchantmentLevelSetEvent, Flow, Hooks,
    HostSide, LevelHost, LevelRandomHost, LivingDeathEvent, LivingDropsEvent,
    LivingIncomingDamageEvent, PlayerBreakSpeedEvent, PlayerContainerCloseEvent,
    PlayerContainerOpenEvent, PlayerRespawnedEvent, PrepareItemEnchantEvent, RegisterCommandsEvent,
    Registrar, RegistrationContext, RunModCommandEvent, ServerHost, ServerStartedEvent,
    ServerStoppingEvent, ServerTickEvent, SweepAttackEvent, Text,
};

#[derive(Default)]
struct EldrichSurge {
    config: config::EldrichConfig,
    combat: combat::CombatState,
    survival: survival::SurvivalState,
}

#[mod_api::horizon_mod]
impl Hooks for EldrichSurge {
    fn register(&self, r: &mut dyn Registrar, _cx: &RegistrationContext) {
        advanced_table::register(r);
        spell_books::register(r);
    }

    fn server_started(&self, host: &mut dyn ServerHost, _event: &mut ServerStartedEvent) {
        let world_dir = host.world_dir();
        let instance_dir = Some(host.instance_dir());
        let enchlib_loaded = host.on("enchlib");
        enchlib_bridge::EnchLibState::load_for_world(
            world_dir.as_deref(),
            instance_dir.as_deref(),
            enchlib_loaded,
        );
        if let Some(ref w) = world_dir {
            self.config.load_from_world(w);
            advanced_table::EnchantingReagentStorage::load_from_world(w);
        }
    }

    fn server_stopping(&self, host: &mut dyn ServerHost, _event: &mut ServerStoppingEvent) {
        if let Some(w) = host.world_dir() {
            self.config.save_to_world(&w);
            advanced_table::EnchantingReagentStorage::save_to_world(&w);
        }
        spell_books::on_server_stopping();
        enchlib_bridge::EnchLibState::reset_to_defaults();
    }

    fn server_tick(&self, host: &mut dyn ServerHost, _event: &mut ServerTickEvent<'_>) {
        spell_books::on_server_tick(host);
    }

    fn block_drops(&self, host: &dyn LevelHost, event: &mut BlockDropsEvent<'_>) -> Flow {
        advanced_table::on_block_drops(host, event)
    }

    fn prepare_item_enchant(
        &self,
        host: &mut dyn ServerHost,
        event: &mut PrepareItemEnchantEvent<'_>,
    ) {
        advanced_table::on_prepare_item_enchant(host, event);
    }

    fn enchantment_level_set(
        &self,
        host: &mut dyn ServerHost,
        event: &mut EnchantmentLevelSetEvent<'_>,
    ) {
        advanced_table::on_enchantment_level_set(host, event);
    }

    fn player_container_open(
        &self,
        host: &mut dyn ServerHost,
        event: &mut PlayerContainerOpenEvent<'_>,
    ) {
        advanced_table::on_player_container_open(host, event);
    }

    fn player_container_close(
        &self,
        host: &mut dyn ServerHost,
        event: &mut PlayerContainerCloseEvent<'_>,
    ) {
        advanced_table::on_player_container_close(host, event);
    }

    fn register_commands(&self, _: &mut dyn ServerHost, event: &mut RegisterCommandsEvent<'_>) {
        commands::register(event);
    }

    fn run_mod_command(
        &self,
        host: &mut dyn ServerHost,
        event: &mut RunModCommandEvent<'_>,
    ) -> Flow {
        let Some(res) = commands::handle(event, &self.config) else {
            return Flow::Continue;
        };
        let _ = host
            .commands()
            .feedback(Text::literal(res.unwrap_or_else(|e| e)));
        Flow::Cancel
    }

    fn living_incoming_damage(
        &self,
        level: &mut dyn LevelRandomHost,
        event: &mut LivingIncomingDamageEvent<'_>,
    ) -> Flow {
        self.combat.on_incoming_damage(level, event, &self.config)
    }

    fn attack_entity(&self, host: &mut dyn ServerHost, event: &mut AttackEntityEvent<'_>) -> Flow {
        self.combat.on_attack_entity(host, event)
    }

    fn sweep_attack(&self, host: HostSide<'_>, event: &mut SweepAttackEvent<'_>) -> Flow {
        let HostSide::Server(host) = host else {
            return Flow::Continue;
        };
        combat::on_sweep_attack(host, event, &self.config)
    }

    fn living_death(&self, host: &mut dyn ServerHost, event: &mut LivingDeathEvent<'_>) -> Flow {
        self.survival.on_living_death(host, event);
        Flow::Continue
    }

    fn living_drops(&self, host: &mut dyn ServerHost, event: &mut LivingDropsEvent<'_>) -> Flow {
        self.survival.on_living_drops(host, event);
        Flow::Continue
    }

    fn player_respawned(&self, host: &mut dyn ServerHost, event: &mut PlayerRespawnedEvent<'_>) {
        self.survival.on_player_respawned(host, event);
    }

    fn player_break_speed(&self, host: HostSide<'_>, event: &mut PlayerBreakSpeedEvent) -> Flow {
        let HostSide::Server(host) = host else {
            return Flow::Continue;
        };
        self.survival.on_break_speed(host, event)
    }

    fn anvil_update(&self, host: HostSide<'_>, event: &mut AnvilUpdateEvent<'_>) -> Flow {
        let HostSide::Server(host) = host else {
            return Flow::Continue;
        };
        anvil::on_anvil_update(host, event)
    }
}
