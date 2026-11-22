mod commands;
mod config;

use mod_api::{Flow, Hooks, RegisterCommandsEvent, RunModCommandEvent, ServerHost, Text};

#[derive(Default)]
struct EldrichSurge {
    config: config::EldrichConfig,
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
}
