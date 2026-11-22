mod combat;
mod commands;
mod config;
mod enchants;
mod survival;

use mod_api::{
    AttackEntityEvent, Flow, Hooks, HostSide, LevelRandomHost, LivingDeathEvent, LivingDropsEvent,
    LivingIncomingDamageEvent, PlayerBreakSpeedEvent, PlayerRespawnedEvent, RegisterCommandsEvent,
    RunModCommandEvent, ServerHost, SweepAttackEvent, Text,
};

#[derive(Default)]
struct EldrichSurge {
    config: config::EldrichConfig,
    combat: combat::CombatState,
    survival: survival::SurvivalState,
}

#[mod_api::horizon_mod]
impl Hooks for EldrichSurge {
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
        self.combat.on_incoming_damage(level, event)
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
}
