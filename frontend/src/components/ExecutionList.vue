<template>
  <div class="execution-list">
    <div v-if="executions.length === 0" class="execution-list-empty">
      <div class="execution-list-empty-icon">
        <svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
          <rect x="2" y="3" width="20" height="14" rx="2" ry="2"></rect>
          <line x1="8" y1="21" x2="16" y2="21"></line>
          <line x1="12" y1="17" x2="12" y2="21"></line>
        </svg>
      </div>
      <h3 class="execution-list-empty-title">Aucune exécution</h3>
      <p class="execution-list-empty-text">Aucune exécution n'a été lancée récemment.</p>
    </div>
    
    <div v-else class="execution-list-table-container">
      <table class="execution-list-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Nom</th>
            <th>Pipeline</th>
            <th>Date de début</th>
            <th>Statut</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="execution in executions" :key="execution.identifier" class="execution-list-row">
            <td class="execution-list-id">{{ execution.identifier.substring(0, 8) }}</td>
            <td class="execution-list-name">{{ execution.name }}</td>
            <td class="execution-list-pipeline">{{ execution.pipelineIdentifier }}</td>
            <td class="execution-list-date">{{ formatDate(execution.startDate) }}</td>
            <td class="execution-list-status">
              <span class="status-badge" :class="getStatusClass(execution.status)">
                {{ execution.status }}
              </span>
            </td>
            <td class="execution-list-actions">
              <button class="execution-list-action-btn" title="Voir les détails">
                <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                  <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path>
                  <circle cx="12" cy="12" r="3"></circle>
                </svg>
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
const props = defineProps({
  executions: {
    type: Array,
    required: true
  }
});

const formatDate = (timestamp) => {
  if (!timestamp) return 'N/A';
  const date = new Date(timestamp);
  return new Intl.DateTimeFormat('fr-FR', {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  }).format(date);
};

const getStatusClass = (status) => {
  switch (status?.toLowerCase()) {
    case 'running':
    case 'en cours':
      return 'status-info';
    case 'completed':
    case 'terminé':
      return 'status-success';
    case 'failed':
    case 'échec':
      return 'status-error';
    default:
      return 'status-warning';
  }
};
</script>

<style scoped>
.execution-list {
  width: 100%;
}

.execution-list-empty {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 4rem 2rem;
  text-align: center;
  background-color: var(--white);
  border-radius: var(--radius);
  box-shadow: var(--shadow);
}

.execution-list-empty-icon {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  background-color: var(--light-blue);
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--primary-blue);
  margin-bottom: 1.5rem;
}

.execution-list-empty-title {
  font-size: 1.5rem;
  font-weight: 600;
  color: var(--text-dark);
  margin-bottom: 0.5rem;
}

.execution-list-empty-text {
  color: var(--text-light);
  max-width: 300px;
}

.execution-list-table-container {
  width: 100%;
  overflow-x: auto;
  background-color: var(--white);
  border-radius: var(--radius);
  box-shadow: var(--shadow);
}

.execution-list-table {
  width: 100%;
  border-collapse: collapse;
}

.execution-list-table th,
.execution-list-table td {
  padding: 1rem;
  text-align: left;
}

.execution-list-table th {
  background-color: var(--light-blue);
  color: var(--primary-blue-dark);
  font-weight: 600;
  font-size: 0.875rem;
  text-transform: uppercase;
  letter-spacing: 0.05em;
}

.execution-list-table th:first-child {
  border-top-left-radius: var(--radius);
}

.execution-list-table th:last-child {
  border-top-right-radius: var(--radius);
}

.execution-list-row {
  border-bottom: 1px solid var(--gray);
  transition: background-color 0.2s ease;
}

.execution-list-row:hover {
  background-color: var(--gray-light);
}

.execution-list-row:last-child {
  border-bottom: none;
}

.execution-list-id {
  font-family: monospace;
  font-weight: 600;
  color: var(--primary-blue-dark);
}

.execution-list-name {
  font-weight: 500;
}

.execution-list-pipeline {
  color: var(--text-light);
  font-size: 0.875rem;
}

.execution-list-date {
  color: var(--text-light);
  font-size: 0.875rem;
}

.execution-list-actions {
  display: flex;
  gap: 0.5rem;
  justify-content: flex-end;
}

.execution-list-action-btn {
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

.execution-list-action-btn:hover {
  background-color: var(--primary-blue);
  color: var(--white);
}
</style>