import api from './api';

export async function listContent(path: string): Promise<any[]> {
    try {
        const response = await api.get('/path' + path + '?action=list');
        return response.data;
    } catch (error) {
        console.error(`Erreur lors de la récupération du contenu du dossier ${path}:`, error);
        return [];
    }
}

export async function listDirectory(path: string): Promise<any[]> {
    try {
        const content = await listContent(path);
        return content.filter((item) => item.isDirectory);
    } catch (error) {
        console.error(`Erreur lors de la récupération des dossiers dans ${path}:`, error);
        return [];
    }
}

export async function listElements(path: string): Promise<any[]> {
    try {
        const content = await listContent(path);
        return content.filter((item) => !item.isDirectory);
    } catch (error) {
        console.error(`Erreur lors de la récupération des fichiers dans ${path}:`, error);
        return [];
    }
}

export async function exists(path: string): Promise<boolean> {
    try {
        const response = await api.get(`/path/exists`, {
            params: { path }
        });
        return response.data.exists;
    } catch (error) {
        console.error(`Erreur lors de la vérification de l'existence du chemin ${path}:`, error);
        return false;
    }
}

export async function getPathProperties(path: string): Promise<any> {
    try {
        const response = await api.get(`/path/properties`, {
            params: { path }
        });
        return response.data;
    } catch (error) {
        console.error(`Erreur lors de la récupération des propriétés du chemin ${path}:`, error);
        return {};
    }
}
