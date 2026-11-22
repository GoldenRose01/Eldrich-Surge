use crate::config::{EldrichConfig, LethalPoisonMode};
use mod_api::{Command, CommandArgumentValue, RegisterCommandsEvent, RunModCommandEvent};
use std::sync::atomic::Ordering;

pub fn register(event: &mut RegisterCommandsEvent<'_>) {
    for root in ["eldrich", "eldritch"] {
        for usage in [
            format!("{root} keep_loyalty_tridents <value:word>"),
            format!("{root} sweeping_requires_enchantment <value:word>"),
            format!("{root} lethal_poison <value:word>"),
        ] {
            event.commands.push(Command::op(&usage, 2));
        }
    }
}

pub fn handle(event: &RunModCommandEvent<'_>, config: &EldrichConfig) -> Option<Result<String, String>> {
    let [root, setting] = event.command.literals.as_slice() else {
        return None;
    };
    if root != "eldrich" && root != "eldritch" {
        return None;
    }
    let Some(CommandArgumentValue::String(raw_value)) = event.command.argument("value") else {
        return Some(Err("Specificare un valore.".into()));
    };

    let res = match setting.as_str() {
        "lethal_poison" => match LethalPoisonMode::parse(raw_value) {
            Some(mode) => {
                config.set_lethal_poison(mode);
                Ok(format!("lethal_poison impostato su: {}.", mode.as_str()))
            }
            None => Err(format!("Valore non valido per lethal_poison: {raw_value}.")),
        },
        "keep_loyalty_tridents" => match parse_bool(raw_value) {
            Some(val) => {
                config.keep_loyalty_tridents.store(val, Ordering::Relaxed);
                Ok(format!(
                    "keep_loyalty_tridents impostato su: {}.",
                    if val { "on" } else { "off" }
                ))
            }
            None => Err(format!("Valore non valido: {raw_value}.")),
        },
        "sweeping_requires_enchantment" => match parse_bool(raw_value) {
            Some(val) => {
                config
                    .sweeping_requires_enchantment
                    .store(val, Ordering::Relaxed);
                Ok(format!(
                    "sweeping_requires_enchantment impostato su: {}.",
                    if val { "on" } else { "off" }
                ))
            }
            None => Err(format!("Valore non valido: {raw_value}.")),
        },
        _ => Err(format!("Impostazione sconosciuta: {setting}.")),
    };
    Some(res)
}

fn parse_bool(raw: &str) -> Option<bool> {
    match raw.to_ascii_lowercase().as_str() {
        "on" | "true" | "1" | "enable" => Some(true),
        "off" | "false" | "0" | "disable" => Some(false),
        _ => None,
    }
}
