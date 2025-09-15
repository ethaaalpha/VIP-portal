import { createRouter, createWebHistory } from 'vue-router';
import type { RouteRecordRaw } from 'vue-router';
import Home from '@/pages/Home.vue';
import Execution from '@/pages/Execution.vue';
import Monitor from '@/pages/Monitor.vue';
import Launch from '@/pages/Launch.vue';
import Login from '@/pages/Login.vue';

const routes: RouteRecordRaw[] = [
  { path: '/', component: Home },
  { path: '/login', component: Login },
  { path: '/launch/:pipeline', name: 'launch', component: Launch },
  { path: '/execution', component: Execution },
  { path: '/monitor', component: Monitor },
];

const router = createRouter({
  history: createWebHistory('/new_front/'),
  routes,
});

export default router;
