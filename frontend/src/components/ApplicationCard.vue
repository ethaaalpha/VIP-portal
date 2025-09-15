<template>
  <div class="app-card" @click="goToLaunch">
    <div class="app-card-header">
      <div class="app-card-title-wrapper">
        <h3 class="app-card-title">{{ title }}</h3>
        <span class="app-card-version">v{{ version }}</span>
      </div>
    </div>
    
    <div class="app-card-content">
      <div class="app-card-pipeline">
        <span class="app-card-label">Pipeline:</span>
        <span class="app-card-value">{{ pipeline }}</span>
      </div>
      
      <div class="app-card-tags">
        <span v-for="(tag, index) in tags" :key="index" class="app-card-tag">
          {{ tag }}
        </span>
      </div>
    </div>
    
    <div class="app-card-footer">
      <button class="app-card-btn">
        <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
          <polygon points="5 3 19 12 5 21 5 3"></polygon>
        </svg>
        Lancer
      </button>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from "vue-router";
import { onMounted } from 'vue';

const props = defineProps({
  title: {
    type: String,
    required: true
  },
  version: {
    type: String,
    required: true
  },
  pipeline: {
    type: String,
    required: true
  },
  tags: {
    type: Array,
    default: () => []
  }
});

const router = useRouter();

const goToLaunch = () => {
  router.push({ name: "launch", params: { pipeline: props.pipeline } });
};
</script>

<style scoped>
.app-card {
  background-color: var(--white);
  border-radius: var(--radius);
  box-shadow: var(--shadow);
  padding: 1.5rem;
  display: flex;
  flex-direction: column;
  gap: 1.25rem;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  cursor: pointer;
  border: 1px solid var(--gray);
  height: 100%;
  width: 13rem;
  overflow: hidden;
}

.app-card:hover {
  transform: translateY(-5px);
  box-shadow: var(--shadow-md);
  border-color: var(--primary-blue-light);
}

.app-card-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  overflow: hidden;
}

.app-card-title-wrapper {
  gap: 0.25rem;
}

.app-card-title {
  font-size: 1.25rem;
  font-weight: 600;
  color: var(--primary-blue-dark);
  margin: 0;
  white-space: nowrap;
}

.app-card-version {
  font-size: 0.75rem;
  color: var(--text-light);
  background-color: var(--light-blue);
  padding: 0.125rem 0.5rem;
  border-radius: 9999px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
}

.app-card-icon {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background-color: var(--light-blue);
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--primary-blue);
}

.app-card-content {
  display: flex;
  flex-direction: column;
  gap: 1rem;
  flex: 1;
}

.app-card-pipeline {
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
  white-space: nowrap;
}

.app-card-label {
  font-size: 0.75rem;
  color: var(--text-light);
  font-weight: 500;
}

.app-card-value {
  font-size: 0.875rem;
  color: var(--text-dark);
  font-weight: 500;
}

.app-card-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
}

.app-card-tag {
  font-size: 0.75rem;
  color: var(--primary-blue);
  background-color: var(--light-blue);
  padding: 0.25rem 0.5rem;
  border-radius: 9999px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
}

.app-card-footer {
  display: flex;
  justify-content: flex-end;
}

.app-card-btn {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  background-color: var(--primary-blue);
  color: var(--white);
  border: none;
  padding: 0.5rem 1rem;
  border-radius: var(--radius);
  font-size: 0.875rem;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 0.2s ease;
}

.app-card-btn:hover {
  background-color: var(--primary-blue-dark);
}
</style>