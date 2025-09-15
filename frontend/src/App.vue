<template>
  <div class="app-container">
    <Navbar />
    <main class="main-content">
      <RouterView v-slot="{ Component }">
        <Transition name="fade" mode="out-in">
          <component :is="Component" />
        </Transition>
      </RouterView>
    </main>
    <Footer />
  </div>
</template>

<script setup>
import { RouterView } from "vue-router";
import Navbar from "@/components/Navbar.vue";
import Footer from "@/components/Footer.vue";
</script>

<style>
:root {
  /* Palette de couleurs bleues et blanches */
  --primary-blue: #1e40af;
  --primary-blue-light: #3b82f6;
  --primary-blue-dark: #1e3a8a;
  --secondary-blue: #60a5fa;
  --accent-blue: #93c5fd;
  --light-blue: #dbeafe;
  --white: #ffffff;
  --off-white: #f8fafc;
  --gray-light: #f1f5f9;
  --gray: #e2e8f0;
  --text-dark: #1e293b;
  --text-light: #64748b;
  --success: #10b981;
  --warning: #f59e0b;
  --error: #ef4444;
  
  /* Ombres */
  --shadow-sm: 0 1px 2px rgba(0, 0, 0, 0.05);
  --shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);
  --shadow-md: 0 10px 15px -3px rgba(0, 0, 0, 0.1), 0 4px 6px -2px rgba(0, 0, 0, 0.05);
  
  /* Arrondis */
  --radius-sm: 0.25rem;
  --radius: 0.5rem;
  --radius-lg: 0.75rem;
  --radius-xl: 1rem;
}

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', sans-serif;
  background-color: var(--off-white);
  color: var(--text-dark);
  line-height: 1.6;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

.app-container {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.main-content {
  flex: 1;
  padding: 2rem;
  max-width: 1400px;
  width: 100%;
  margin: 0 auto;
}

/* Animations de transition */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

/* Styles globaux */
h1, h2, h3, h4, h5, h6 {
  font-weight: 600;
  line-height: 1.2;
  margin-bottom: 1rem;
  color: var(--primary-blue-dark);
}

h1 {
  font-size: 2rem;
}

h2 {
  font-size: 1.75rem;
}

h3 {
  font-size: 1.5rem;
}

a {
  color: var(--primary-blue);
  text-decoration: none;
  transition: color 0.2s ease;
}

a:hover {
  color: var(--primary-blue-light);
}

button, .btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  padding: 0.5rem 1rem;
  font-size: 0.875rem;
  font-weight: 500;
  border-radius: var(--radius);
  transition: all 0.2s ease;
  cursor: pointer;
  border: none;
}

.btn-primary {
  background-color: var(--primary-blue);
  color: var(--white);
}

.btn-primary:hover {
  background-color: var(--primary-blue-dark);
}

.btn-secondary {
  background-color: var(--white);
  color: var(--primary-blue);
  border: 1px solid var(--primary-blue);
}

.btn-secondary:hover {
  background-color: var(--light-blue);
}

.card {
  background-color: var(--white);
  border-radius: var(--radius);
  box-shadow: var(--shadow);
  padding: 1.5rem;
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.card:hover {
  transform: translateY(-4px);
  box-shadow: var(--shadow-md);
}

.status-badge {
  display: inline-flex;
  align-items: center;
  padding: 0.25rem 0.75rem;
  border-radius: 9999px;
  font-size: 0.75rem;
  font-weight: 500;
}

.status-success {
  background-color: rgba(16, 185, 129, 0.1);
  color: var(--success);
}

.status-warning {
  background-color: rgba(245, 158, 11, 0.1);
  color: var(--warning);
}

.status-error {
  background-color: rgba(239, 68, 68, 0.1);
  color: var(--error);
}

.status-info {
  background-color: rgba(59, 130, 246, 0.1);
  color: var(--primary-blue);
}

.form-group {
  margin-bottom: 1.5rem;
}

.form-label {
  display: block;
  margin-bottom: 0.5rem;
  font-weight: 500;
  color: var(--text-dark);
}

.form-input {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid var(--gray);
  border-radius: var(--radius);
  background-color: var(--white);
  transition: border-color 0.2s ease, box-shadow 0.2s ease;
}

.form-input:focus {
  outline: none;
  border-color: var(--primary-blue-light);
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.2);
}

.form-error {
  color: var(--error);
  font-size: 0.875rem;
  margin-top: 0.25rem;
}

.container {
  width: 100%;
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 1rem;
}

.section {
  margin-bottom: 3rem;
}

.section-title {
  display: flex;
  align-items: center;
  margin-bottom: 1.5rem;
  padding-bottom: 0.5rem;
  border-bottom: 2px solid var(--light-blue);
}

.section-title h2 {
  margin-bottom: 0;
}

.grid {
  display: grid;
  gap: 1.5rem;
}

.grid-cols-1 {
  grid-template-columns: 1fr;
}

@media (min-width: 640px) {
  .grid-cols-2 {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (min-width: 768px) {
  .grid-cols-3 {
    grid-template-columns: repeat(3, 1fr);
  }
}

@media (min-width: 1024px) {
  .grid-cols-4 {
    grid-template-columns: repeat(4, 1fr);
  }
}
</style>