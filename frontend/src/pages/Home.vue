<template>
  <div class="home-page">
    <div class="home-header">
      <h1 class="home-title">Tableau de bord</h1>
      <p class="home-subtitle">Bienvenue sur le portail VIP. Gérez vos applications et suivez vos exécutions.</p>
    </div>
    
    <div class="home-search-container">
      <SearchBar v-model:searchQuery="searchQuery" />
    </div>
    
    <div class="home-section">
      <div class="section-title">
        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="section-title-icon">
          <path d="M16 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"></path>
          <circle cx="8.5" cy="7" r="4"></circle>
          <line x1="20" y1="8" x2="20" y2="14"></line>
          <line x1="23" y1="11" x2="17" y2="11"></line>
        </svg>
        <h2>Applications fréquemment utilisées</h2>
      </div>
      
      <div v-if="frequentlyUsedApps.length === 0" class="home-empty-state">
        <p>Aucune application fréquemment utilisée pour le moment.</p>
      </div>
      
      <FrequentlyUsedApplications v-else :applications="frequentlyUsedApps" />
    </div>
    
    <div class="home-section">
      <div class="section-title">
        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="section-title-icon">
          <path d="M21 16V8a2 2 0 0 0-1-1.73l-7-4a2 2 0 0 0-2 0l-7 4A2 2 0 0 0 3 8v8a2 2 0 0 0 1 1.73l7 4a2 2 0 0 0 2 0l7-4A2 2 0 0 0 21 16z"></path>
          <polyline points="3.27 6.96 12 12.01 20.73 6.96"></polyline>
          <line x1="12" y1="22.08" x2="12" y2="12"></line>
        </svg>
        <h2>Toutes les applications</h2>
      </div>
      
      <div v-if="isLoading" class="home-loading">
        <div class="home-loading-spinner"></div>
        <p>Chargement des applications...</p>
      </div>
      
      <ApplicationList v-else :applications="applications" :searchQuery="searchQuery" />
    </div>
    
    <div class="home-section">
      <div class="section-title">
        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="section-title-icon">
          <polyline points="22 12 18 12 15 21 9 3 6 12 2 12"></polyline>
        </svg>
        <h2>Exécutions récentes</h2>
      </div>
      
      <div v-if="isLoadingExecutions" class="home-loading">
        <div class="home-loading-spinner"></div>
        <p>Chargement des exécutions...</p>
      </div>
      
      <ExecutionList v-else :executions="executions" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from "vue";
import SearchBar from "@/components/SearchBar.vue";
import ApplicationList from "@/components/ApplicationList.vue";
import FrequentlyUsedApplications from "@/components/FrequentlyUsedApplications.vue";
import ExecutionList from "@/components/ExecutionList.vue";
import { fetchApplications, fetchExecutions } from "@/services/api";

const applications = ref([]);
const executions = ref([]);
const searchQuery = ref("");
const isLoading = ref(true);
const isLoadingExecutions = ref(true);

// Simuler les applications fréquemment utilisées (à remplacer par une vraie logique)
const frequentlyUsedApps = computed(() => {
  return applications.value.slice(0, 4);
});

onMounted(async () => {
  try {
    applications.value = await fetchApplications();
  } catch (error) {
    console.error("Erreur lors du chargement des applications:", error);
  } finally {
    isLoading.value = false;
  }
  
  try {
    executions.value = await fetchExecutions();
  } catch (error) {
    console.error("Erreur lors du chargement des exécutions:", error);
  } finally {
    isLoadingExecutions.value = false;
  }
});
</script>

<style scoped>
.home-page {
  display: flex;
  flex-direction: column;
  gap: 2rem;
}

.home-header {
  margin-bottom: 1rem;
}

.home-title {
  font-size: 2rem;
  font-weight: 700;
  color: var(--primary-blue-dark);
  margin-bottom: 0.5rem;
}

.home-subtitle {
  color: var(--text-light);
  font-size: 1rem;
}

.home-search-container {
  margin-bottom: 1rem;
}

.home-section {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.section-title {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  padding-bottom: 0.75rem;
  border-bottom: 2px solid var(--light-blue);
}

.section-title h2 {
  font-size: 1.5rem;
  font-weight: 600;
  color: var(--primary-blue-dark);
  margin: 0;
}

.section-title-icon {
  color: var(--primary-blue);
}

.home-empty-state {
  background-color: var(--white);
  border-radius: var(--radius);
  padding: 2rem;
  text-align: center;
  color: var(--text-light);
  box-shadow: var(--shadow);
}

.home-loading {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 3rem;
  background-color: var(--white);
  border-radius: var(--radius);
  box-shadow: var(--shadow);
}

.home-loading-spinner {
  width: 40px;
  height: 40px;
  border: 3px solid var(--light-blue);
  border-radius: 50%;
  border-top-color: var(--primary-blue);
  animation: spin 1s linear infinite;
  margin-bottom: 1rem;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}
</style>