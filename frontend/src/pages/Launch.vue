<script setup lang="ts">
import { fetchApplication, launchPipeline } from '@/services/api';
import { useRoute, useRouter } from 'vue-router';
import { ref, onMounted } from 'vue';
import { listDirectory, listElements } from '@/services/storage';


const route = useRoute();
const router = useRouter();
const pipeline = Array.isArray(route.params.pipeline)
    ? route.params.pipeline[0]
    : route.params.pipeline;

const pipelineDef = ref<any | null>(null);
const formInputs = ref<Record<string, string>>({});
const errors = ref<Record<string, string>>({});
const errorMessage = ref<string | null>(null);
const showFileExplorer = ref(false);
const currentPath = ref('/vip'); // Dossier de départ
const directories = ref<any[]>([]);
const files = ref<any[]>([]);
const selectedFileInput = ref<string | null>(null); // Stocke l'input en cours de sélection

// Charger les détails du pipeline
onMounted(async () => {
    pipelineDef.value = await fetchApplication(pipeline);

    if (pipelineDef.value) {
        formInputs.value = pipelineDef.value.parameters.reduce(
            (acc: Record<string, string>, param: any) => {
                acc[param.name] = param.defaultValue || '';
                return acc;
            },
            {}
        );
    }
});

// Fonction pour charger le contenu d'un dossier
const loadDirectory = async (path: string) => {
    currentPath.value = path;
    directories.value = await listDirectory(path);
    console.log(directories.value);
    files.value = await listElements(path);
};

// Ouvrir l'explorateur pour un input spécifique
const openFileExplorer = (paramName: string) => {
    selectedFileInput.value = paramName;
    showFileExplorer.value = true;
    loadDirectory(currentPath.value);
};

// Sélectionner un fichier et fermer l'explorateur
const selectFile = (filePath: string) => {
    if (selectedFileInput.value) {
        formInputs.value[selectedFileInput.value] = filePath;
        showFileExplorer.value = false;
    }
};

// Fonction de validation
const validateInputs = () => {
    errors.value = {}; // Réinitialisation des erreurs

    pipelineDef.value?.parameters.forEach((param: any) => {
        if (!param.isOptional && !formInputs.value[param.name]) {
            errors.value[param.name] = 'Ce champ est requis.';
        }

        if (
            param.type === 'String' &&
            typeof formInputs.value[param.name] !== 'string'
        ) {
            errors.value[param.name] = 'Doit être une chaîne de caractères.';
        }
    });

    return Object.keys(errors.value).length === 0;
};

// Gérer la soumission du pipeline
const submitPipeline = async () => {
    if (!validateInputs()) return;

    errorMessage.value = null;

    try {
        let inputValues: Record<string, string> = {};
        
        for (const [key, value] of Object.entries(formInputs.value)) {
            console.log(key, value);
            inputValues[key] = value;
        }
        const formData = {
            name: formInputs.value['name'],
            pipeline,
            inputValues: inputValues
        };
        
        
        console.log("formData", formData);
        console.log(formData);

        const response = await launchPipeline(formData);

        if (response?.identifier) {
            router.push({
                path: '/monitor',
                query: { id: response.identifier }
            });
        } else {
            throw new Error('Aucun identifiant de workflow reçu.');
        }
    } catch (error) {
        console.error('Erreur lors du lancement du pipeline :', error);
        errorMessage.value =
            'Échec du lancement du pipeline. Veuillez réessayer.';
    }
};
</script>

<template>
    <div class="p-6 flex space-x-6 h-full justify-center">
        <!-- Formulaire des paramètres -->
        <div
            class="w-1/3 bg-white shadow-md p-6 rounded-lg border border-gray-300 h-fit mb-6"
        >
            <h2 class="text-2xl font-semibold mb-4">Paramètres d'exécution</h2>
            <form @submit.prevent="submitPipeline" class="space-y-4">
                <div v-if="!pipelineDef" class="text-gray-500">
                    Chargement des informations du pipeline...
                </div>
                <div v-if="pipelineDef" class="flex flex-col">
                    <label class="font-semibold">Nom de l'exécution</label>
                    <p class="text-gray-500 text-sm mb-1">
                        Nom de l'exécution du pipeline
                    </p>
                    <input
                        v-model="formInputs['name']"
                        type="text"
                        class="border p-2 rounded w-full"
                        placeholder="Nom de l'exécution"
                    />
                </div>
                <div
                    v-for="param in pipelineDef?.parameters"
                    :key="param.name"
                    class="flex flex-col"
                >
                    <label class="font-semibold">{{ param.name }}</label>
                    <p class="text-gray-500 text-sm mb-1" v-html="param.description"></p>

                    <input
                        v-if="param.type === 'String'"
                        v-model="formInputs[param.name]"
                        type="text"
                        class="border p-2 rounded w-full"
                    />

                    <div
                        v-if="param.type === 'File'"
                        class="flex items-center space-x-2"
                    >
                        <input
                            v-model="formInputs[param.name]"
                            type="text"
                            class="border p-2 rounded w-full mr-2"
                            disabled
                        />
                        <button
                            type="button"
                            @click="openFileExplorer(param.name)"
                            class="bg-blue-500 text-white px-4 py-2 rounded whitespace-nowrap ml-2"
                        >
                            Choisir un fichier
                        </button>
                    </div>

                    <p v-if="errors[param.name]" class="text-red-500 text-sm">
                        {{ errors[param.name] }}
                    </p>
                </div>

                <p v-if="errorMessage" class="text-red-500 text-sm mt-2">
                    {{ errorMessage }}
                </p>

                <button
                    type="submit"
                    class="bg-blue-500 text-white px-4 py-2 rounded w-full mt-4"
                >
                    Lancer le pipeline
                </button>
            </form>
        </div>
        <!-- Détails du pipeline -->
        <div
            class="w-1/3 bg-gray-100 shadow-md p-6 rounded-lg border border-gray-300 h-fit mb-6"
        >
            <h2 class="text-3xl font-bold">
                {{ pipelineDef?.name || 'Chargement...' }}
            </h2>
            <p class="mt-4 text-gray-600" v-html="pipelineDef?.description || 'Chargement des informations du pipeline...'"></p>
        </div>

        <!-- Explorateur de fichiers -->
        <div
            v-if="showFileExplorer"
            class="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center"
        >
            <div class="bg-white p-6 rounded-lg shadow-lg w-96">
                <h3 class="text-lg font-semibold mb-2">
                    Sélectionner un fichier
                </h3>
                <p class="text-sm text-gray-600">
                    Chemin actuel: {{ currentPath }}
                </p>
                <ul class="mt-4 space-y-2">
                    <li
                        v-for="dir in directories"
                        :key="dir.path"
                        @click="loadDirectory(dir.path)"
                        class="cursor-pointer text-blue-500"
                    >
                        📁 {{ dir.path.split('/').pop() }}
                    </li>
                    <li
                        v-for="file in files"
                        :key="file.path"
                        @click="selectFile(file.path)"
                        class="cursor-pointer text-gray-700"
                    >
                        📄 {{ file.path.split('/').pop() }}
                    </li>
                </ul>
                <button
                    @click="showFileExplorer = false"
                    class="mt-4 px-4 py-2 bg-red-500 text-white rounded"
                >
                    Annuler
                </button>
            </div>
        </div>
    </div>
</template>
