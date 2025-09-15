# Projet Vite Vue

## Description
Proof of concept du portail VIP à l'aide de Vite, Vue 3 et TailwindCSS. Il est entièrement basé sur l'API de VIP pour récupérer les informations nécessaires.

## Installation et configuration

### 1. Installer les dépendances
```bash
npm install
```
### 2. Lancer TailwindCSS
Dans un autre terminal, lancer la commande suivante pour compiler TailwindCSS dynamiquement :
```bash
npx tailwindcss -i ./src/index.css -o ./dist/tailwind.css --watch
```
### 3. Lancer le serveur
```bash
npm run dev
```
### 4. Lancer le proxy
```bash
node proxy.cjs
```