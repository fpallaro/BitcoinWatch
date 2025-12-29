# Bitcoin Watch - Wear OS App

App per Galaxy Watch 5 Pro che mostra il valore del Bitcoin in tempo reale.

## Funzionalità
- Prezzo BTC in USD aggiornato ogni 30 secondi
- Variazione percentuale 24h (verde/rosso)
- Orario ultimo aggiornamento
- Design ottimizzato per display rotondi

## Come ottenere l'APK

### Metodo 1: GitHub Actions (Automatico)
1. Fai fork di questa repository
2. Vai su "Actions" tab
3. Clicca "Build APK" workflow
4. Clicca "Run workflow"
5. Al termine, scarica l'APK da "Artifacts"

### Metodo 2: Build locale
Richiede Android Studio con Wear OS SDK.

```bash
./gradlew assembleDebug
```

## Installazione su Galaxy Watch
1. Abilita "Debug ADB" sul watch (Impostazioni > Info > tap 7x su "Versione software")
2. Connetti watch via WiFi o cavo
3. Installa con: `adb install app-debug.apk`

## API utilizzata
CoinGecko API (gratuita, no API key richiesta)

## Requisiti
- Galaxy Watch 5 Pro (Wear OS 4+)
- Connessione internet sul watch
