<template>
  <div class="execution-page">
    <div class="execution-header">
      <h1 class="execution-title">Lancer une exécution</h1>
      <p class="execution-subtitle">Sélectionnez une application et configurez les paramètres pour lancer une exécution.</p>
    </div>
    
    <div class="execution-card">
      <div class="execution-step">
        <div class="execution-step-number">1</div>
        <h2 class="execution-step-title">Sélectionner une application</h2>
      </div>
      
      <div class="execution-app-selector">
        <div class="form-group">
          <label for="app-select" class="form-label">Application</label>
          <select
            id="app-select"
            v-model="selectedApp"
            class="form-input"
            @change="updateInputs"
          >
            <option value="" disabled selected>Sélectionnez une application</option>
            <option v-for="app in applications" :key="app" :value="app">
              {{ app }}
            </option>
          </select>
        </div>
      </div>
      
      <div v-if="selectedApp" class="execution-step execution-step-parameters">
        <div class="execution-step-number">2</div>
        <h2 class="execution-step-title">Configurer les paramètres</h2>
      </div>
      
      <div v-if="selectedApp && inputs.length > 0" class="execution-parameters">
        <div v-for="(input, index) in inputs" :key="index" class="form-group">
          <label :for="`param-${index}`" class="form-label">{{ input.name }}</label>
          <input
            :id="`param-${index}`"
            type="text"
            class="form-input"
            :placeholder="`Entrez ${input.name}`"
          />
        </div>
      </div>
      
      <div v-if="selectedApp" class="execution-step">
        <div class="execution-step-number">3</div>
        <h2 class="execution-step-title">Lancer l'exécution</h2>
      </div>
      
      <div v-if="selectedApp" class="execution-actions">
        <button class="btn btn-secondary" @click="resetForm">
          <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="btn-icon">
            <path d="M2.5 2v6h6M2.66 15.57a10 10 0 1 0 .57-8.38"></path>
          </svg>
          Réinitialiser
        </button>
        <button class="btn btn-primary" @click="launchExecution">
          <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="btn-icon">
            <polygon points="5 3 19 12 5 21 5 3"></polygon>
          </svg>
          Lancer l'exécution
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";

interface InputField {
  name: string;
}

const applications = ["App A", "App B", "App C"];
const selectedApp = ref<string | null>(null);
const inputs = ref<InputField[]>([]);

const updateInputs = () => {
  switch (selectedApp.value) {
    case "App A":
      inputs.value = [{ name: "Paramètre 1" }, { name: "Paramètre 2" }];
      break;
    case "App B":
      inputs.value = [{ name: "Option X" }, { name: "Option Y" }];
      break;
    default:
      inputs.value = [];
  }
};

const resetForm = () => {
  selectedApp.value = null;
  inputs.value = [];
};

const launchExecution = () => {
  // Logique pour lancer l'exécution
  alert(`Exécution lancée pour ${selectedApp.value}`);
};
</script>

<style scoped>
.execution-page {
  display: flex;
  flex-direction: column;
  gap: 2rem;
}

.execution-header {
  margin-bottom: 1rem;
}

.execution-title {
  font-size: 2rem;
  font-weight: 700;
  color: var(--primary-blue-dark);
  margin-bottom: 0.5rem;
}

.execution-subtitle {
  color: var(--text-light);
  font-size: 1rem;
}

.execution-card {
  background-color: var(--white);
  border-radius: var(--radius);
  box-shadow: var(--shadow);
  padding: 2rem;
  display: flex;
  flex-direction: column;
  gap: 2rem;
}

.execution-step {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.execution-step-number {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background-color: var(--primary-blue);
  color: var(--white);
  font-weight: 600;
  font-size: 1.125rem;
}

.execution-step-title {
  font-size: 1.25rem;
  font-weight: 600;
  color: var(--primary-blue-dark);
  margin: 0;
}

.execution-app-selector,
.execution-parameters {
  padding-left: 3rem;
}

.execution-actions {
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
  margin-top: 1rem;
}

.btn-icon {
  margin-right: 0.5rem;
}

.execution-step-parameters {
  margin-top: 1rem;
}

@media (max-width: 768px) {
  .execution-card {
    padding: 1.5rem;
  }
  
  .execution-app-selector,
  .execution-parameters {
    padding-left: 0;
  }
  
  .execution-actions {
    flex-direction: column;
    gap: 0.75rem;
  }
  
  .execution-actions button {
    width: 100%;
  }
}
</style>