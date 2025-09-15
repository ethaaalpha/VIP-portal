<template>
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
      placeholder="Rechercher une application..."
      :value="searchQuery"
      @input="updateSearchQuery"
    />
    <button v-if="searchQuery" class="search-bar-clear" @click="clearSearch">
      <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
        <line x1="18" y1="6" x2="6" y2="18"></line>
        <line x1="6" y1="6" x2="18" y2="18"></line>
      </svg>
    </button>
  </div>
</template>

<script setup>
import { defineProps, defineEmits } from "vue";

const props = defineProps({
  searchQuery: {
    type: String,
    default: ""
  }
});

const emit = defineEmits(["update:searchQuery"]);

const updateSearchQuery = (event) => {
  emit("update:searchQuery", event.target.value);
};

const clearSearch = () => {
  emit("update:searchQuery", "");
};
</script>

<style scoped>
.search-bar {
  display: flex;
  align-items: center;
  background-color: var(--white);
  border: 1px solid var(--gray);
  border-radius: var(--radius);
  padding: 0 1rem;
  transition: border-color 0.2s ease, box-shadow 0.2s ease;
  width: 100%;
}

.search-bar:focus-within {
  border-color: var(--primary-blue-light);
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.2);
}

.search-bar-icon {
  color: var(--text-light);
  margin-right: 0.75rem;
}

.search-bar-input {
  flex: 1;
  border: none;
  padding: 0.75rem 0;
  font-size: 0.875rem;
  background-color: transparent;
  color: var(--text-dark);
}

.search-bar-input:focus {
  outline: none;
}

.search-bar-input::placeholder {
  color: var(--text-light);
}

.search-bar-clear {
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: transparent;
  border: none;
  color: var(--text-light);
  cursor: pointer;
  padding: 0.25rem;
  margin-left: 0.5rem;
  border-radius: 50%;
  transition: background-color 0.2s ease, color 0.2s ease;
}

.search-bar-clear:hover {
  background-color: var(--gray-light);
  color: var(--text-dark);
}
</style>