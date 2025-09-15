<template>
    <header class="navbar">
      <div class="container navbar-container">
        <router-link to="/" class="navbar-logo">
          <img src="/vip-logo.png" alt="VIP Logo" class="navbar-logo-img" />
          <h1 class="navbar-title">VIP Portal</h1>
        </router-link>
  
        <div class="navbar-mobile-toggle" @click="toggleMobileMenu">
          <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <line x1="3" y1="12" x2="21" y2="12"></line>
            <line x1="3" y1="6" x2="21" y2="6"></line>
            <line x1="3" y1="18" x2="21" y2="18"></line>
          </svg>
        </div>
  
        <nav class="navbar-nav" :class="{ 'navbar-nav-active': isMobileMenuOpen }">
          <ul class="navbar-nav-list">
            <li class="navbar-nav-item">
              <router-link to="/" class="navbar-nav-link" @click="closeMobileMenu">
                <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="navbar-nav-icon">
                  <path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"></path>
                  <polyline points="9 22 9 12 15 12 15 22"></polyline>
                </svg>
                Accueil
              </router-link>
            </li>
            <li class="navbar-nav-item">
              <router-link to="/execution" class="navbar-nav-link" @click="closeMobileMenu">
                <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="navbar-nav-icon">
                  <polygon points="5 3 19 12 5 21 5 3"></polygon>
                </svg>
                Lancer une exécution
              </router-link>
            </li>
            <li class="navbar-nav-item">
              <router-link to="/monitor" class="navbar-nav-link" @click="closeMobileMenu">
                <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="navbar-nav-icon">
                  <rect x="2" y="3" width="20" height="14" rx="2" ry="2"></rect>
                  <line x1="8" y1="21" x2="16" y2="21"></line>
                  <line x1="12" y1="17" x2="12" y2="21"></line>
                </svg>
                Monitoring
              </router-link>
            </li>
          </ul>
          <div class="navbar-actions">
            <button v-if="auth.isLoggedIn" class="btn btn-primary navbar-btn" @click="onLogout">
              <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="navbar-btn-icon">
                <path d="M16 17l5-5-5-5"/>
                <path d="M21 12H9"/>
                <path d="M13 19a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h6a2 2 0 0 1 2 2"/>
              </svg>
              Logout
            </button>
            <router-link v-else to="/login" class="btn btn-primary navbar-btn">
              <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="navbar-btn-icon">
                <path d="M15 3h4a2 2 0 0 1 2 2v14a2 2 0 0 1-2 2h-4"></path>
                <polyline points="10 17 15 12 10 7"></polyline>
                <line x1="15" y1="12" x2="3" y2="12"></line>
              </svg>
              Connexion
            </router-link>
          </div>
        </nav>
      </div>
    </header>
  </template>
  
  <script setup>
  import { ref, onMounted, watch } from 'vue';
  import { useRouter, useRoute } from 'vue-router';
  import { logoutSession, clearVipCookies } from '@/services/api';
  import { useAuthStore } from '@/stores/auth';

  const router = useRouter();
  const route = useRoute();
  const auth = useAuthStore();
  const isMobileMenuOpen = ref(false);

  const toggleMobileMenu = () => {
    isMobileMenuOpen.value = !isMobileMenuOpen.value;
  };

  const closeMobileMenu = () => {
    isMobileMenuOpen.value = false;
  };

  const hasSessionCookie = () => {
    return document.cookie.split(';').some(c => c.trim().startsWith('vip-cookie-session='));
  };

  const onLogout = async () => {
    try {
      await logoutSession();
    } catch (e) {
      console.error('Erreur lors de la déconnexion :', e);
    } finally {
      // clearVipCookies();
      auth.setLoggedIn(false);
      window.location.reload()
    }
  };

  onMounted(() => {
    auth.setLoggedIn(hasSessionCookie());
  });

  watch(() => route.fullPath, () => {
    // Met à jour l'état si les cookies changent pendant la navigation
    auth.setLoggedIn(hasSessionCookie());
  });
  </script>
  
  <style scoped>
  .navbar {
    background-color: var(--primary-blue);
    box-shadow: var(--shadow);
    position: sticky;
    top: 0;
    z-index: 100;
  }
  
  .navbar-container {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 1rem;
    height: 70px;
  }
  
  .navbar-logo {
    display: flex;
    align-items: center;
    gap: 0.75rem;
    color: var(--white);
    text-decoration: none;
  }
  
  .navbar-logo-img {
    width: 40px;
    height: 40px;
    border-radius: 50%;
    background-color: var(--white);
    padding: 0.25rem;
    object-fit: contain;
    transition: transform 0.3s ease;
  }
  
  .navbar-logo:hover .navbar-logo-img {
    transform: rotate(10deg);
  }
  
  .navbar-title {
    font-size: 1.25rem;
    font-weight: 700;
    margin: 0;
    color: var(--white);
  }
  
  .navbar-mobile-toggle {
    display: none;
    color: var(--white);
    cursor: pointer;
  }
  
  .navbar-nav {
    display: flex;
    align-items: center;
    gap: 2rem;
  }
  
  .navbar-nav-list {
    display: flex;
    list-style: none;
    gap: 1.5rem;
    margin: 0;
    padding: 0;
  }
  
  .navbar-nav-link {
    display: flex;
    align-items: center;
    gap: 0.5rem;
    color: var(--white);
    font-weight: 500;
    padding: 0.5rem 0;
    position: relative;
    transition: color 0.2s ease;
  }
  
  .navbar-nav-link::after {
    content: '';
    position: absolute;
    bottom: 0;
    left: 0;
    width: 0;
    height: 2px;
    background-color: var(--white);
    transition: width 0.3s ease;
  }
  
  .navbar-nav-link:hover::after,
  .router-link-active::after {
    width: 100%;
  }
  
  .navbar-nav-icon {
    opacity: 0.8;
  }
  
  .navbar-actions {
    display: flex;
    align-items: center;
    gap: 1rem;
  }
  
  .navbar-btn {
    display: flex;
    align-items: center;
    gap: 0.5rem;
    background-color: rgba(255, 255, 255, 0.1);
    color: var(--white);
    border: 1px solid rgba(255, 255, 255, 0.2);
    padding: 0.5rem 1rem;
    border-radius: var(--radius);
    transition: background-color 0.2s ease;
  }
  
  .navbar-btn:hover {
    background-color: rgba(255, 255, 255, 0.2);
  }
  
  .navbar-btn-icon {
    opacity: 0.8;
  }
  
  @media (max-width: 768px) {
    .navbar-mobile-toggle {
      display: block;
    }
  
    .navbar-nav {
      position: fixed;
      top: 70px;
      left: 0;
      right: 0;
      background-color: var(--primary-blue);
      flex-direction: column;
      align-items: flex-start;
      padding: 1rem;
      gap: 1rem;
      box-shadow: var(--shadow-md);
      transform: translateY(-100%);
      opacity: 0;
      pointer-events: none;
      transition: transform 0.3s ease, opacity 0.3s ease;
    }
  
    .navbar-nav-active {
      transform: translateY(0);
      opacity: 1;
      pointer-events: auto;
    }
  
    .navbar-nav-list {
      flex-direction: column;
      width: 100%;
    }
  
    .navbar-nav-item {
      width: 100%;
    }
  
    .navbar-nav-link {
      padding: 0.75rem 0;
    }
  
    .navbar-actions {
      width: 100%;
    }
  
    .navbar-btn {
      width: 100%;
      justify-content: center;
    }
  }
  </style>