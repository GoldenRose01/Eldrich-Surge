use serde::{Deserialize, Serialize};
use std::path::Path;
use std::sync::atomic::{AtomicBool, Ordering};
use std::sync::Mutex;

#[derive(Debug, Clone, Copy, PartialEq, Eq)]
pub enum LethalPoisonMode {
    Off,
    Player,
    Mob,
    All,
}

impl LethalPoisonMode {
    pub fn as_str(self) -> &'static str {
        match self {
            Self::Off => "off",
            Self::Player => "player",
            Self::Mob => "mob",
            Self::All => "all",
        }
    }

    pub fn parse(raw: &str) -> Option<Self> {
        match raw.to_ascii_lowercase().as_str() {
            "off" | "false" | "0" | "disable" => Some(Self::Off),
            "player" | "players" => Some(Self::Player),
            "mob" | "mobs" => Some(Self::Mob),
            "all" | "on" | "true" | "1" | "enable" => Some(Self::All),
            _ => None,
        }
    }
}

pub struct EldrichConfig {
    pub keep_loyalty_tridents: AtomicBool,
    pub sweeping_requires_enchantment: AtomicBool,
    pub lethal_poison: Mutex<LethalPoisonMode>,
}

impl Default for EldrichConfig {
    fn default() -> Self {
        Self {
            keep_loyalty_tridents: AtomicBool::new(false),
            sweeping_requires_enchantment: AtomicBool::new(true),
            lethal_poison: Mutex::new(LethalPoisonMode::Off),
        }
    }
}

impl EldrichConfig {
    pub fn set_lethal_poison(&self, mode: LethalPoisonMode) {
        *self.lethal_poison.lock().expect("lethal_poison lock") = mode;
    }

    pub fn lethal_poison_mode(&self) -> LethalPoisonMode {
        *self.lethal_poison.lock().expect("lethal_poison lock")
    }

    pub fn save_to_world(&self, world_dir: &Path) {
        #[derive(Serialize)]
        struct Snapshot {
            keep_loyalty_tridents: bool,
            sweeping_requires_enchantment: bool,
            lethal_poison: String,
        }
        let snap = Snapshot {
            keep_loyalty_tridents: self.keep_loyalty_tridents.load(Ordering::Relaxed),
            sweeping_requires_enchantment: self
                .sweeping_requires_enchantment
                .load(Ordering::Relaxed),
            lethal_poison: self.lethal_poison_mode().as_str().to_owned(),
        };
        let path = world_dir.join("eldrich-surge/config.json");
        if let Some(parent) = path.parent() {
            let _ = std::fs::create_dir_all(parent);
        }
        if let Ok(json) = serde_json::to_string_pretty(&snap) {
            let _ = std::fs::write(path, json);
        }
    }

    pub fn load_from_world(&self, world_dir: &Path) {
        #[derive(Deserialize)]
        struct Snapshot {
            keep_loyalty_tridents: Option<bool>,
            sweeping_requires_enchantment: Option<bool>,
            lethal_poison: Option<String>,
        }
        let path = world_dir.join("eldrich-surge/config.json");
        let Ok(text) = std::fs::read_to_string(path) else {
            return;
        };
        let Ok(snap) = serde_json::from_str::<Snapshot>(&text) else {
            return;
        };
        if let Some(v) = snap.keep_loyalty_tridents {
            self.keep_loyalty_tridents.store(v, Ordering::Relaxed);
        }
        if let Some(v) = snap.sweeping_requires_enchantment {
            self.sweeping_requires_enchantment
                .store(v, Ordering::Relaxed);
        }
        if let Some(raw) = snap.lethal_poison
            && let Some(mode) = LethalPoisonMode::parse(&raw)
        {
            self.set_lethal_poison(mode);
        }
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn parses_lethal_poison_modes() {
        assert_eq!(LethalPoisonMode::parse("off"), Some(LethalPoisonMode::Off));
        assert_eq!(LethalPoisonMode::parse("player"), Some(LethalPoisonMode::Player));
        assert_eq!(LethalPoisonMode::parse("mob"), Some(LethalPoisonMode::Mob));
        assert_eq!(LethalPoisonMode::parse("all"), Some(LethalPoisonMode::All));
        assert_eq!(LethalPoisonMode::parse("invalid"), None);
    }

    #[test]
    fn config_persists_to_world_dir() {
        let dir = std::env::temp_dir().join("eldrich_surge_cfg_test");
        let _ = std::fs::remove_dir_all(&dir);
        let cfg = EldrichConfig::default();
        cfg.keep_loyalty_tridents.store(true, Ordering::Relaxed);
        cfg.sweeping_requires_enchantment
            .store(false, Ordering::Relaxed);
        cfg.set_lethal_poison(LethalPoisonMode::All);
        cfg.save_to_world(&dir);

        let loaded = EldrichConfig::default();
        loaded.load_from_world(&dir);
        assert!(loaded.keep_loyalty_tridents.load(Ordering::Relaxed));
        assert!(!loaded.sweeping_requires_enchantment.load(Ordering::Relaxed));
        assert_eq!(loaded.lethal_poison_mode(), LethalPoisonMode::All);
        let _ = std::fs::remove_dir_all(&dir);
    }
}
