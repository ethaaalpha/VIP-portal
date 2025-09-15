<template>
  <div class="app-list">
    <div v-if="filteredApplications.length === 0" class="app-list-empty">
      <div class="app-list-empty-icon">
        <svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
          <circle cx="11" cy="11" r="8"></circle>
          <line x1="21" y1="21" x2="16.65" y2="16.65"></line>
        </svg>
      </div>
      <h3 class="app-list-empty-title">Aucune application trouvée</h3>
      <p class="app-list-empty-text">Aucune application ne correspond à votre recherche.</p>
    </div>
    
    <div v-else class="flex flex-wrap justify-center mt-6 gap-4 overflow-y-auto h-full overflow-x-hidden p-2 gap-4">
      <ApplicationCard
        v-for="app in filteredApplications"
        :key="app.identifier"
        :title="app.name"
        :version="app.version"
        :pipeline="app.identifier"
        :tags="app.tags"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import ApplicationCard from "@/components/ApplicationCard.vue";
import { computed } from "vue";

const props = defineProps({
  applications: {
    type: Array,
    required: true
  },
  searchQuery: {
    type: String,
    default: ""
  }
});

const filteredApplications = computed(() => {
  const query = props.searchQuery.toLowerCase();
  return props.applications.filter((app) => app.name.toLowerCase().includes(query));
});
</script>

<style scoped>
.app-list {
  width: 100%;
}

.app-list-grid {
  display: grid;
  grid-template-columns: repeat(1, 1fr);
  gap: 1.5rem;
}

.app-list-empty {
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

.app-list-empty-icon {
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

.app-list-empty-title {
  font-size: 1.5rem;
  font-weight: 600;
  color: var(--text-dark);
  margin-bottom: 0.5rem;
}

.app-list-empty-text {
  color: var(--text-light);
  max-width: 300px;
}

@media (min-width: 640px) {
  .app-list-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (min-width: 1024px) {
  .app-list-grid {
    grid-template-columns: repeat(3, 1fr);
  }
}

@media (min-width: 1280px) {
  .app-list-grid {
    grid-template-columns: repeat(4, 1fr);
  }
}
</style>