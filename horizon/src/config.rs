use std::sync::atomic::AtomicBool;
use std::sync::Mutex;

#[derive(Clone, Copy, PartialEq, Eq)]
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
}
