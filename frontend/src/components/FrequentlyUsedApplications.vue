<script setup lang="ts">
import ApplicationCard from "@/components/ApplicationCard.vue";

const props = defineProps<{
  applications: Array<{ name: string; version: string; identifier: string; tags: string[] }>;
}>();
</script>

<template>
  <div class="flex flex-col justify-center mt-4 pb-4 rounded-lg p-4 h-1/3">
    <h2 class="text-xl font-semibold text-center">Applications régulièrement utilisées</h2>
    <div class="flex flex-wrap justify-center mt-6 gap-4 overflow-y-auto h-full overflow-x-hidden p-2">
      <ApplicationCard
        v-for="app in applications.slice(0, 5)"
        :key="app.identifier"
        :title="app.name"
        :version="app.version"
        :pipeline="app.identifier"
        :tags="[app.version]"
      />
    </div>
  </div>
</template>
