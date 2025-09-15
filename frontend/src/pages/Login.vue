<template>
  <div class="login-page">
    <div class="login-container">
      <div class="login-card">
        <div class="login-header">
          <img src="/vip-logo.png" alt="VIP Logo" class="login-logo" />
          <h1 class="login-title">Connexion</h1>
          <p class="login-subtitle">Connectez-vous à votre compte VIP Portal</p>
        </div>
        
        <form @submit.prevent="handleLogin" id="login-form" class="login-form">
          <div class="form-group">
            <label for="username" class="form-label">Nom d'utilisateur</label>
            <div class="login-input-wrapper">
              <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="login-input-icon">
                <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"></path>
                <circle cx="12" cy="7" r="4"></circle>
              </svg>
              <input
                id="username"
                type="text"
                name="username"
                v-model="username"
                class="login-input"
                placeholder="Entrez votre nom d'utilisateur"
                autocomplete="username"
                required
              />
            </div>
          </div>
          
          <div class="form-group">
            <label for="password" class="form-label">Mot de passe</label>
            <div class="login-input-wrapper">
              <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="login-input-icon">
                <rect x="3" y="11" width="18" height="11" rx="2" ry="2"></rect>
                <path d="M7 11V7a5 5 0 0 1 10 0v4"></path>
              </svg>
              <input
                id="password"
                :type="showPassword ? 'text' : 'password'"
                name="password"
                v-model="password"
                class="login-input"
                placeholder="Entrez votre mot de passe"
                autocomplete="current-password"
                required
              />
              <button
                type="button"
                class="login-password-toggle"
                @click="togglePasswordVisibility"
                tabindex="-1"
              >
                <svg v-if="showPassword" xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                  <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path>
                  <circle cx="12" cy="12" r="3"></circle>
                </svg>
                <svg v-else xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                  <path d="M17.94 17.94A10.07 10.07 0 0 1 12 20c-7 0-11-8-11-8a18.45 18.45 0 0 1 5.06-5.94M9.9 4.24A9.12 9.12 0 0 1 12 4c7 0 11 8 11 8a18.5 18.5 0 0 1-2.16 3.19m-6.72-1.07a3 3 0 1 1-4.24-4.24"></path>
                  <line x1="1" y1="1" x2="23" y2="23"></line>
                </svg>
              </button>
            </div>
          </div>
          
          <div class="login-options">
            <div class="login-remember">
              <input type="checkbox" id="remember" class="login-checkbox" v-model="remember" />
              <label for="remember" class="login-checkbox-label">Se souvenir de moi</label>
            </div>
            <a href="#" class="login-forgot-link">Mot de passe oublié ?</a>
          </div>
          
          <div v-if="errorMessage" class="login-error">
            <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="login-error-icon">
              <circle cx="12" cy="12" r="10"></circle>
              <line x1="12" y1="8" x2="12" y2="12"></line>
              <line x1="12" y1="16" x2="12.01" y2="16"></line>
            </svg>
            {{ errorMessage }}
          </div>
          
          <button type="submit" class="login-button">
            <span>Se connecter</span>
            <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="login-button-icon">
              <path d="M15 3h4a2 2 0 0 1 2 2v14a2 2 0 0 1-2 2h-4"></path>
              <polyline points="10 17 15 12 10 7"></polyline>
              <line x1="15" y1="12" x2="3" y2="12"></line>
            </svg>
          </button>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { loginSession, setVipCookies } from '@/services/api';
import { useAuthStore } from '@/stores/auth';

const router = useRouter();
const auth = useAuthStore();

const username = ref('');
const password = ref('');
const errorMessage = ref('');
const showPassword = ref(false);
const remember = ref(true);

const togglePasswordVisibility = () => {
  showPassword.value = !showPassword.value;
};

const handleLogin = async () => {
  errorMessage.value = '';

  if (!username.value || !password.value) {
    errorMessage.value = 'Veuillez remplir tous les champs.';
    return;
  }

  try {
      await loginSession(username.value, password.value);
   // const days = remember.value ? 1 : 0.04; // ~1h
   // setVipCookies(username.value, data.httpHeaderValue, days);
    auth.setLoggedIn(true);
    await router.replace('/');
  } catch (error: any) {
    console.error('Erreur lors de l\'authentification :', error);
    errorMessage.value = error?.response?.statusText || 'Identifiants incorrects.';
  }
};
</script>

<style scoped>
.login-page {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: calc(100vh - 70px - 200px);
  padding: 2rem;
}

.login-container {
  width: 100%;
  max-width: 420px;
}

.login-card {
  background-color: var(--white);
  border-radius: var(--radius);
  box-shadow: var(--shadow-md);
  padding: 2rem;
  border: 1px solid var(--gray);
}

.login-header {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  margin-bottom: 2rem;
}

.login-logo {
  width: 64px;
  height: 64px;
  border-radius: 50%;
  background-color: var(--white);
  padding: 0.5rem;
  margin-bottom: 1rem;
  box-shadow: var(--shadow);
}

.login-title {
  font-size: 1.75rem;
  font-weight: 700;
  color: var(--primary-blue-dark);
  margin-bottom: 0.5rem;
}

.login-subtitle {
  color: var(--text-light);
  font-size: 0.875rem;
}

.login-form {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.login-input-wrapper {
  position: relative;
  display: flex;
  align-items: center;
}

.login-input-icon {
  position: absolute;
  left: 1rem;
  color: var(--text-light);
}

.login-input {
  width: 100%;
  padding: 0.75rem 1rem 0.75rem 2.75rem;
  border: 1px solid var(--gray);
  border-radius: var(--radius);
  background-color: var(--white);
  transition: border-color 0.2s ease, box-shadow 0.2s ease;
}

.login-input:focus {
  outline: none;
  border-color: var(--primary-blue-light);
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.2);
}

.login-password-toggle {
  position: absolute;
  right: 1rem;
  background: none;
  border: none;
  color: var(--text-light);
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
}

.login-options {
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 0.875rem;
}

.login-remember {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.login-checkbox {
  width: 16px;
  height: 16px;
  border-radius: 4px;
  border: 1px solid var(--gray);
  accent-color: var(--primary-blue);
}

.login-checkbox-label {
  color: var(--text-light);
  cursor: pointer;
}

.login-forgot-link {
  color: var(--primary-blue);
  text-decoration: none;
  transition: color 0.2s ease;
}

.login-forgot-link:hover {
  color: var(--primary-blue-dark);
  text-decoration: underline;
}

.login-error {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.75rem;
  background-color: rgba(239, 68, 68, 0.1);
  color: var(--error);
  border-radius: var(--radius);
  font-size: 0.875rem;
}

.login-error-icon {
  flex-shrink: 0;
}

.login-button {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;
  background-color: var(--primary-blue);
  color: var(--white);
  border: none;
  padding: 0.75rem 1.5rem;
  border-radius: var(--radius);
  font-size: 1rem;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 0.2s ease;
}

.login-button:hover {
  background-color: var(--primary-blue-dark);
}

.login-button-icon {
  transition: transform 0.2s ease;
}

.login-button:hover .login-button-icon {
  transform: translateX(3px);
}
</style>