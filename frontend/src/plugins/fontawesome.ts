import { library } from '@fortawesome/fontawesome-svg-core';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';

// Importer les icônes nécessaires
import { faUser, faCog, faTrash, faEdit, faCheck, faCube } from '@fortawesome/free-solid-svg-icons';

// Ajouter les icônes dans la bibliothèque FontAwesome
library.add(faUser, faCog, faTrash, faEdit, faCheck, faCube);

export default FontAwesomeIcon;
