<template>
  <div class="monitor-page">
    <div class="monitor-header">
      <h1 class="monitor-title">Monitoring des exécutions</h1>
      <p class="monitor-subtitle">Suivez l'état et les performances de vos exécutions en cours et terminées.</p>
    </div>

    <div class="monitor-stats">
      <div class="monitor-stat-card">
        <div class="monitor-stat-icon monitor-stat-icon-running">
          <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <circle cx="12" cy="12" r="10"></circle>
            <polyline points="12 6 12 12 16 14"></polyline>
          </svg>
        </div>
        
        <div class="monitor-stat-content">
          <h3 class="monitor-stat-title">En cours</h3>
          <p class="monitor-stat-value">{{ getExecutionsByStatus('En cours').length }}</p>
        </div>
      </div>
      
      <div class="monitor-stat-card">
        <div class="monitor-stat-icon monitor-stat-icon-completed">
          <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path>
            <polyline points="22 4 12 14.01 9 11.01"></polyline>
          </svg>
        </div>
        <div class="monitor-stat-content">
          <h3 class="monitor-stat-title">Terminées</h3>
          <p class="monitor-stat-value">{{ getExecutionsByStatus('Terminé').length }}</p>
        </div>
      </div>
      
      <div class="monitor-stat-card">
        <div class="monitor-stat-icon monitor-stat-icon-failed">
          <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <circle cx="12" cy="12" r="10"></circle>
            <line x1="12" y1="8" x2="12" y2="12"></line>
            <line x1="12" y1="16" x2="12.01" y2="16"></line>
          </svg>
        </div>
        <div class="monitor-stat-content">
          <h3 class="monitor-stat-title">Échecs</h3>
          <p class="monitor-stat-value">{{ getExecutionsByStatus('Échec').length }}</p>
        </div>
      </div>
      
      <div class="monitor-stat-card">
        <div class="monitor-stat-icon monitor-stat-icon-total">
          <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <polyline points="22 12 16 12 14 15 10 15 8 12 2 12"></polyline>
            <path d="M5.45 5.11L2 12v6a2 2 0 0 0 2 2h16a2 2 0 0 0 2-2v-6l-3.45-6.89A2 2 0 0 0 16.76 4H7.24a2 2 0 0 0-1.79 1.11z"></path>
          </svg>
        </div>
        <div class="monitor-stat-content">
          <h3 class="monitor-stat-title">Total</h3>
          <p class="monitor-stat-value">{{ executions.length }}</p>
        </div>
      </div>
    </div>
    
    <div class="monitor-filters">
      <div class="monitor-search">
        <div class="search-bar">
          <div class="search-bar-icon">
            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <circle cx="11" cy="11" r="8"></circle>
              <line x1="21" y1="21" x2="16.65" y2="16.65"></line>
            </svg>
          </div>
          <input
            type="text"
            class="search-bar-input"
            placeholder="Rechercher une exécution..."
            v-model="searchQuery"
          />
          <button v-if="searchQuery" class="search-bar-clear" @click="searchQuery = ''">
            <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <line x1="18" y1="6" x2="6" y2="18"></line>
              <line x1="6" y1="6" x2="18" y2="18"></line>
            </svg>
          </button>
        </div>
      </div>
      
      <div class="monitor-filter-buttons">
        <button 
          class="monitor-filter-btn" 
          :class="{ 'monitor-filter-btn-active': statusFilter === 'all' }"
          @click="statusFilter = 'all'"
        >
          Tous
        </button>
        <button 
          class="monitor-filter-btn" 
          :class="{ 'monitor-filter-btn-active': statusFilter === 'En cours' }"
          @click="statusFilter = 'En cours'"
        >
          En cours
        </button>
        <button 
          class="monitor-filter-btn" 
          :class="{ 'monitor-filter-btn-active': statusFilter === 'Terminé' }"
          @click="statusFilter = 'Terminé'"
        >
          Terminés
        </button>
        <button 
          class="monitor-filter-btn" 
          :class="{ 'monitor-filter-btn-active': statusFilter === 'Échec' }"
          @click="statusFilter = 'Échec'"
        >
          Échecs
        </button>
      </div>
    </div>
    
    <div class="monitor-table-container">
      <table class="monitor-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Application</th>
            <th>Statut</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="execution in filteredExecutions" :key="execution.id" class="monitor-table-row">
            <td class="monitor-table-id">{{ execution.id }}</td>
            <td class="monitor-table-app">{{ execution.app }}</td>
            <td class="monitor-table-status">
              <span class="status-badge" :class="getStatusClass(execution.status)">
                {{ execution.status }}
              </span>
            </td>
            <td class="monitor-table-actions">
              <button class="monitor-action-btn" title="Voir les détails">
                <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                  <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path>
                  <circle cx="12" cy="12" r="3"></circle>
                </svg>
              </button>
              <button class="monitor-action-btn" title="Télécharger les résultats">
                <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                  <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"></path>
                  <polyline points="7 10 12 15 17 10"></polyline>
                  <line x1="12" y1="15" x2="12" y2="3"></line>
                </svg>
                <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                  <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"></path>
                  <polyline points="7 10 12 15 17 10"></polyline>
                  <line x1="12" y1="15" x2="12" y2="3"></line>
                </svg>
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from "vue";

interface Execution {
  id: number;
  app: string;
  status: "En cours" | "Terminé" | "Échec";
}

const executions = ref<Execution[]>([
  { id: 1, app: "App A", status: "En cours" },
  { id: 2, app: "App B", status: "Terminé" },
  { id: 3, app: "App C", status: "Échec" },
  { id: 4, app: "App A", status: "Terminé" },
  { id: 5, app: "App B", status: "En cours" },
]);

const searchQuery = ref("");
const statusFilter = ref("all");

const filteredExecutions = computed(() => {
  let result = executions.value;
  
  // Filtrer par statut
  if (statusFilter.value !== "all") {
    result = result.filter(execution => execution.status === statusFilter.value);
  }
  
  // Filtrer par recherche
  if (searchQuery.value) {
    const query = searchQuery.value.toLowerCase();
    result = result.filter(execution => 
      execution.app.toLowerCase().includes(query) || 
      execution.id.toString().includes(query)
    );
  }
  
  return result;
});

const getExecutionsByStatus = (status: "En cours" | "Terminé" | "Échec") => {
  return executions.value.filter(execution => execution.status === status);
};

const getStatusClass = (status: string) => {
  switch (status) {
    case "En cours":
      return "status-info";
    case "Terminé":
      return "status-success";
    case "Échec":
      return "status-error";
    default:
      return "";
  }
};
</script>

<style scoped>
.monitor-page {
  display: flex;
  flex-direction: column;
  gap: 2rem;
}

.monitor-header {
  margin-bottom: 1rem;
}

.monitor-title {
  font-size: 2rem;
  font-weight: 700;
  color: var(--primary-blue-dark);
  margin-bottom: 0.5rem;
}

.monitor-subtitle {
  color: var(--text-light);
  font-size: 1rem;
}

.monitor-stats {
  display: grid;
  grid-template-columns: repeat(1, 1fr);
  gap: 1rem;
}

.monitor-stat-card {
  background-color: var(--white);
  border-radius: var(--radius);
  box-shadow: var(--shadow);
  padding: 1.5rem;
  display: flex;
  align-items: center;
  gap: 1rem;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.monitor-stat-card:hover {
  transform: translateY(-3px);
  box-shadow: var(--shadow-md);
}

.monitor-stat-icon {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.monitor-stat-icon-running {
  background-color: rgba(59, 130, 246, 0.1);
  color: var(--primary-blue);
}

.monitor-stat-icon-completed {
  background-color: rgba(16, 185, 129, 0.1);
  color: var(--success);
}

.monitor-stat-icon-failed {
  background-color: rgba(239, 68, 68, 0.1);
  color: var(--error);
}

.monitor-stat-icon-total {
  background-color: rgba(107, 114, 128, 0.1);
  color: var(--text-dark);
}

.monitor-stat-content {
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
}

.monitor-stat-title {
  font-size: 0.875rem;
  font-weight: 500;
  color: var(--text-light);
  margin: 0;
}

.monitor-stat-value {
  font-size: 1.5rem;
  font-weight: 700;
  color: var(--text-dark);
  margin: 0;
}

.monitor-filters {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.monitor-search {
  width: 100%;
}

.monitor-filter-buttons {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
}

.monitor-filter-btn {
  background-color: var(--white);
  border: 1px solid var(--gray);
  border-radius: var(--radius);
  padding: 0.5rem 1rem;
  font-size: 0.875rem;
  font-weight: 500;
  color: var(--text-dark);
  cursor: pointer;
  transition: all 0.2s ease;
}

.monitor-filter-btn:hover {
  background-color: var(--gray-light);
}

.monitor-filter-btn-active {
  background-color: var(--primary-blue);
  color: var(--white);
  border-color: var(--primary-blue);
}

.monitor-table-container {
  width: 100%;
  overflow-x: auto;
  background-color: var(--white);
  border-radius: var(--radius);
  box-shadow: var(--shadow);
}

.monitor-table {
  width: 100%;
  border-collapse: collapse;
}

.monitor-table th,
.monitor-table td {
  padding: 1rem;
  text-align: left;
}

.monitor-table th {
  background-color: var(--light-blue);
  color: var(--primary-blue-dark);
  font-weight: 600;
  font-size: 0.875rem;
  text-transform: uppercase;
  letter-spacing: 0.05em;
}

.monitor-table-row {
  border-bottom: 1px solid var(--gray);
  transition: background-color 0.2s ease;
}

.monitor-table-row:hover {
  background-color: var(--gray-light);
}

.monitor-table-row:last-child {
  border-bottom: none;
}

.monitor-table-id {
  font-family: monospace;
  font-weight: 600;
}

.monitor-table-app {
  font-weight: 500;
}

.monitor-table-actions {
  display: flex;
  gap: 0.5rem;
}

.monitor-action-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background-color: var(--light-blue);
  color: var(--primary-blue);
  border: none;
  cursor: pointer;
  transition: background-color 0.2s ease, color 0.2s ease;
}

.monitor-action-btn:hover {
  background-color: var(--primary-blue);
  color: var(--white);
}

@media (min-width: 640px) {
  .monitor-stats {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .monitor-filters {
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
  }
  
  .monitor-search {
    width: auto;
    max-width: 300px;
  }
}

@media (min-width: 1024px) {
  .monitor-stats {
    grid-template-columns: repeat(4, 1fr);
  }
}
</style>