"""Create lightweight, original SVG wiki headers and a formation diagram."""
from pathlib import Path
from html import escape

ROOT = Path(__file__).resolve().parents[2]


def banner(directory, title, kicker, subtitle, accent):
    directory.mkdir(parents=True, exist_ok=True)
    svg = f'''<svg xmlns="http://www.w3.org/2000/svg" width="1280" height="320" viewBox="0 0 1280 320">
    <defs><linearGradient id="bg"><stop stop-color="#131a29"/><stop offset="1" stop-color="#26213d"/></linearGradient></defs>
    <rect width="1280" height="320" rx="20" fill="url(#bg)"/>
    <path d="M0 318H1280" stroke="{accent}" stroke-width="6"/>
    <g stroke="{accent}" fill="none" opacity=".24"><path d="M1050 28L1182 160 1050 292 918 160Z" stroke-width="2"/>
    <path d="M1050 66L1144 160 1050 254 956 160Z" stroke-width="2"/><circle cx="1050" cy="160" r="58" stroke-width="2"/></g>
    <g fill="{accent}"><rect x="1039" y="144" width="22" height="32"/><rect x="1028" y="153" width="44" height="14"/>
    <rect x="1168" y="146" width="12" height="12"/><rect x="922" y="146" width="12" height="12"/>
    <rect x="1044" y="32" width="12" height="12"/><rect x="1044" y="276" width="12" height="12"/></g>
    <g font-family="Segoe UI,Arial,sans-serif"><text x="58" y="65" font-size="17" letter-spacing="4" fill="{accent}">{escape(kicker)}</text>
    <text x="54" y="155" font-size="64" font-weight="700" fill="#f3f1ff">{escape(title)}</text>
    <text x="58" y="207" font-size="23" fill="#c7cad9">{escape(subtitle)}</text>
    <rect x="58" y="247" width="246" height="29" rx="6" fill="#ffffff" opacity=".08"/>
    <text x="72" y="267" font-size="13" letter-spacing="2" fill="#e2e0ee">MINECRAFT 26.3 · FABRIC</text></g></svg>'''
    (directory / 'banner.svg').write_text(svg,encoding='utf-8')


def build():
    assets = ROOT / 'docs/wiki/assets'
    banner(assets,'Eldrich Surge','IL GRIMORIO DEL MONDO','104 incantamenti · Fusioni · Rituali · Regole','#bd93ff')
    banner(ROOT.parent / 'enchlib/docs/wiki/assets','EnchLib','LE REGOLE, NEL TUO MONDO','Configurazione · Categorie · Tavoli · API','#65d9c4')
    pieces = ['<svg xmlns="http://www.w3.org/2000/svg" width="1040" height="550" viewBox="0 0 1040 550">',
        '<rect width="1040" height="550" rx="18" fill="#171d2c"/>',
        '<g font-family="Segoe UI,Arial,sans-serif"><text x="40" y="48" fill="#f3f1ff" font-size="25" font-weight="700">La formazione del tavolo avanzato</text>',
        '<text x="40" y="78" fill="#aeb7ce" font-size="16">Vista dall’alto · Tutti i blocchi alla stessa altezza</text>']
    for z in range(-2,3):
        for x in range(-2,3):
            gold = abs(x)==2 and abs(z)==2
            quartz = (abs(x)==2 and z==0) or (abs(z)==2 and x==0)
            center = x==z==0
            air = abs(x)<=1 and abs(z)<=1 and not center
            color, label = ('#d4ad52','ORO') if gold else ('#dad8e5','QUARZO') if quartz else ('#9b74d5','TAVOLO') if center else ('#263246','ARIA') if air else ('#202737','·')
            left, top = 40+(x+2)*78,115+(z+2)*78
            pieces.append(f'<rect x="{left}" y="{top}" width="70" height="70" rx="7" fill="{color}" stroke="#5b6580" stroke-width="1"/>')
            textcolor = '#171d2c' if gold or quartz else '#e4e5f2'
            pieces.append(f'<text x="{left+35}" y="{top+40}" text-anchor="middle" font-size="11" font-weight="700" fill="{textcolor}">{label}</text>')
    for i,(color,title,detail) in enumerate([
        ('#d4ad52','4 blocchi d’oro','Agli angoli del quadrato esterno'),
        ('#dad8e5','4 blocchi di quarzo cesellato','A due blocchi, sui punti cardinali'),
        ('#9b74d5','1 tavolo avanzato','Tavolo normale + 4 frammenti di ametista'),
        ('#263246','8 spazi d’aria obbligatori','Le posizioni adiacenti al tavolo')]):
        top=130+i*82
        pieces.append(f'<rect x="494" y="{top}" width="20" height="20" rx="4" fill="{color}"/><text x="530" y="{top+16}" font-size="20" font-weight="600" fill="#f3f1ff">{title}</text><text x="530" y="{top+43}" font-size="15" fill="#b5bed3">{detail}</text>')
    pieces.append('<text x="494" y="484" font-size="17" fill="#bd93ff">Sblocca gli slot da 15 e 30 livelli</text></g></svg>')
    (assets / 'formation.svg').write_text('\n'.join(pieces),encoding='utf-8')


if __name__ == '__main__':
    build()
