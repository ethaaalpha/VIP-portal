<template>
    <footer class="footer">
      <div class="container footer-container">
        <div class="footer-brand">
          <router-link to="/" class="footer-logo">
            <img src="/vip-logo.png" alt="VIP Logo" class="footer-logo-img" />
            <span class="footer-logo-text">VIP Portal</span>
          </router-link>
          <p class="footer-description">
            Plateforme d'exécution d'applications d'imagerie médicale. Gérez vos applications et suivez vos exécutions sur l'infrastructure VIP.
          </p>
        </div>
        
        <div class="footer-links">
          <div class="footer-links-group">
            <h3 class="footer-links-title">Navigation</h3>
            <ul class="footer-links-list">
              <li><router-link to="/" class="footer-link">Accueil</router-link></li>
              <li><router-link to="/execution" class="footer-link">Lancer une exécution</router-link></li>
              <li><router-link to="/monitor" class="footer-link">Monitoring</router-link></li>
            </ul>
          </div>
          
          <div class="footer-links-group">
            <h3 class="footer-links-title">Ressources</h3>
            <ul class="footer-links-list">
              <li><a href="#" class="footer-link">Documentation</a></li>
              <li><a href="#" class="footer-link">API</a></li>
              <li><a href="#" class="footer-link">Support</a></li>
            </ul>
          </div>
          
          <div class="footer-links-group">
            <h3 class="footer-links-title">Contact</h3>
            <ul class="footer-links-list">
              <li><a href="mailto:support@vip-portal.com" class="footer-link">vip@creatis.insa-lyon.fr</a></li>
              <li><a href="tel:+33123456789" class="footer-link">+33 1 23 45 67 89</a></li>
            </ul>
          </div>
        </div>
      </div>
      
      <div class="footer-bottom">
        <div class="container">
          <p class="footer-copyright">
            &copy; {{ new Date().getFullYear() }} VIP Portal. CREATIS - CNRS - INSA Lyon - INSERM - Université de Lyon.
          </p>
        </div>
      </div>
    </footer>
  </template>
  
  <style scoped>
  .footer {
    background-color: var(--primary-blue-dark);
    color: var(--white);
    padding-top: 3rem;
    margin-top: 2rem;
  }
  
  .footer-container {
    display: grid;
    grid-template-columns: 1fr;
    gap: 2rem;
    padding-bottom: 2rem;
  }
  
  .footer-brand {
    display: flex;
    flex-direction: column;
    gap: 1rem;
  }
  
  .footer-logo {
    display: flex;
    align-items: center;
    gap: 0.75rem;
    color: var(--white);
    text-decoration: none;
  }
  
  .footer-logo-img {
    width: 32px;
    height: 32px;
    border-radius: 50%;
    background-color: var(--white);
    padding: 0.25rem;
    object-fit: contain;
  }
  
  .footer-logo-text {
    font-size: 1.25rem;
    font-weight: 700;
  }
  
  .footer-description {
    color: var(--gray-light);
    max-width: 300px;
    font-size: 0.875rem;
  }
  
  .footer-links {
    display: grid;
    grid-template-columns: 1fr;
    gap: 2rem;
  }
  
  .footer-links-title {
    color: var(--white);
    font-size: 1rem;
    margin-bottom: 1rem;
    font-weight: 600;
  }
  
  .footer-links-list {
    list-style: none;
    padding: 0;
    margin: 0;
    display: flex;
    flex-direction: column;
    gap: 0.5rem;
  }
  
  .footer-link {
    color: var(--gray-light);
    text-decoration: none;
    font-size: 0.875rem;
    transition: color 0.2s ease;
  }
  
  .footer-link:hover {
    color: var(--white);
    text-decoration: underline;
  }
  
  .footer-bottom {
    border-top: 1px solid rgba(255, 255, 255, 0.1);
    padding: 1.5rem 0;
  }
  
  .footer-copyright {
    color: var(--gray-light);
    font-size: 0.875rem;
    text-align: center;
  }
  
  @media (min-width: 768px) {
    .footer-container {
      grid-template-columns: 1fr 2fr;
    }
    
    .footer-links {
      grid-template-columns: repeat(3, 1fr);
    }
  }
  </style>