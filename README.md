# Notes améliorations Android

## Migration vers `AndroidX`

``AndroidX`` est équivalent à la librairie de support Android, avec tous ses composants regroupé sous un seul namespace, disponible à cette [adresse](https://developer.android.com/jetpack/`AndroidX`/).

Il est recommandé aujourd’hui que les nouveaux projets doivent utilisent `AndroidX` plutôt que la librairie de support car `AndroidX` suit le Semantic Versionning, sera mise à jour dans le futur et toutes les nouvelles librairies ne seront pas disponible dans la librairie de support.

Sachant que le projet avait déjà commencé en utilisant les anciennes librairies de support, nous avons suivi la page [suivante](https://developer.android.com/jetpack/`AndroidX`/migrate) afin de mettre en place `AndroidX` avec un projet existant.

## Mise en place de Material Design

L’une des premières et principale amélioration mise en place la mise en place du dernier Material Design.

Cela permet à l’utilisateur d’avoir une interface graphique intégré avec les autres applications, qu’il connaît et saura donc utiliser plus facilement. C’est une pratique recommandée afin que l’application plaise à l’utilisateur.

Material Design est composé de plusieurs composants et plusieurs d’entre eux ont été mise en place afin qu’elle suive de très près ces recommandations.

Pour commencer, il faut suivre le protocole expliqué dans la documentation, puis rajouter les composants en suivant la [documentation](https://material.io/develop/android/docs/getting-started/).

Le plus simple pour commencer à utiliser Material Design et avoir déjà des fichiers de départ remplis avec les valeurs recommandées de Google est d’utiliser leur projet Material Design Builder à cette [adresse](https://github.com/material-components/material-components-android/tree/master/material-theme-builder)

Ensuite, il reste à ajouter les composants un par un que nous voulons utiliser en suivant les exemples fournis dans la documentation.

## Éléments d’interface commun

Il existe des composants très simple à mettre en place comme les champs de texte ou les boutons.

Chaque composant possède une page d’explication pour mettre en place celui-ci.

Par exemple, voici celui des [champs de texte](https://material.io/develop/android/components/text-input-layout/)

## RecyclerView, SwipeToRefresh et Material Cards

Afin de présenter la liste des conversation et des messages, nous avons choisi de les représenter en utilisant 3 concepts présents dans l’écosystème Android : Les `RecyclerViews`, le SwipteToRefresh et les Material Cards.

Pour commencer, il faut mettre en place un `RecyclerView`.

L’avantage des `RecyclerViews` est la possibilité de créer des listes avec une mise en forme complexe et s’occupe de gérer la complexité du code.
Le plus simple est de suivre les exemples fournis par la [documentation Android](https://developer.android.com/guide/topics/ui/layout/`RecyclerView`)

Il suffit d’ajouter les éléments et de les lier à un layout personnalisé pour que chaque élément de la liste possèdent le layout demandé et les `ViewAdapters` se chargent de lier les données.

Ensuite, nous avons ajouté le `SwipeToRefresh`.

Le principe est de pouvoir rajouter la possibilité de glisser vers le bas pour pouvoir recharger une liste dans l’activité.

Ce principe est très utilisé dans le monde Android et nous l’avons donc implémenté avec les `RecyclerViews`, étant donné qu’ils gèrent les listes de messages ou de conversations.

Le mettre en place est très simple, il suffit de suivre les exemples de la documentation, et ensuite, lors de l'événement de refresh, on rappelle notre API.

Enfin, afin de suivre les règles Material Design, nous avons choisi de mettre en valeur les éléments des listes avec les Cards. Les Cards sont utilisé pour mettre en valeur des listes d'éléments enrichies.

Nous avons mis en place une version très simple et épurée, ne profitant pas de toutes les possibilités offertes.

## Ajout de Snackbar

Une `Snackbar` est le remplacement graphique des `Toasts`. Tandis que les `Toasts` ont une personnalisation limitée, les `Snackbars` peuvent être personnalisés et sont intégré pleinement dans Material Design.

La documentation propose des exemples simples pour mettre en place cela [ici](https://developer.android.com/training/`Snackbar`)

Nous les avons utilisées pour afficher les messages d’erreur lié au réseau avec par exemple, lorsque le système n’est pas connecté à Internet.

## Mise en place de fragments pour les Préférences

Nous avons choisi de mettre en place les fragments sur les préférences car passer par une activité est une opération dépréciée par `AndroidX` (Et la librairie de support).
Pour le mettre en place, il faut simplement suivre la [documentation Android](https://developer.android.com/guide/topics/ui/settings#inflate_the_hierarchy)

# Refonte de l’API vers Rest + JWT
Nous avons également choisi de mettre en place une API Rest et une authentification par JWT afin de mettre en oeuvre les cours de web services de cette année, en utilisation Node JS.

L’API Rest est une interface de programmation d’application qui fait appel à des requêtes HTTP et ses verbes pour obtenir, placer, publier ou encore supprimer des données.

Cela a plusieurs avantages comme la simplicité de maintenance avec une structure universelle des liens, moteur de l’application.
L’absence de gestion d’état du client sur le serveur offre, en plus d’une consommation de mémoire réduite, une plus grande simplicité et offre la possibilité de le scaler à l’infini et une tolérance aux pannes avec l’utilisation d’un load balancer en plus par exemple.

L’utilisation d’URL comme représentant d’une ressource, permet la mise en place de cache serveur et client très facilement.

Son inconvénient majeur est que c’est le client qui a la charge de conserver toutes les informations

Pour tester l’API nous avons utilisé Postman

JSON Web Token (JWT) est un standard ouvert défini dans la RFC 7519. Il permet l'échange sécurisé de jetons (tokens) entre plusieurs parties. Cette sécurité de l’échange se traduit par la vérification de l’intégrité des données à l’aide d’une signature numérique. Elle s’effectue par l'algorithme HMAC ou RSA.

Pour mettre cela en place, nous avons utilisé NodeJS et plusieurs librairies pour manipuler MySQL, JWT ou encore bcrypt pour le hachage des mots de passe.
Plutôt que d’utiliser du JavaScript, nous en avons profité pour utiliser TypeScript afin d’apprendre un langage de plus en plus utilisé avec Node.

## Utilisation de Retrofit + Jackson pour les requêtes

Retrofit est une librairie qui permet d’effectuer des requêtes vers une API de manière beaucoup plus simple.

Elle est basée sur un modèle objet mis en place en amont et de Jackson afin de ne plus avoir à écrire la transcription JSON vers le modèle objet et inversement.

De plus l’utilisation de requêtes asynchrones avec des callbacks permet de ne plus avoir à écrire d’`AsyncTask` et les requêtes à la main mais de juste traiter le cas d’une réponse ou d’un échec.

Lors de la mise en place de Retrofit, nous avons mis en place un `RequestAsyncTask<>` générique utilisant les generics Java.

Cela permet de créer des RequestAsyncTask de n'importe quel type choisi, rendant cette classe générique et supportant n’importe quel type de données sous n’importe quel type, et réponds dans notre cas d’utilisation que la fonction asynchrone de Retrofit ne répondais pas. 

Cela permet de retourner une `AsyncTask` prenant en paramètre d'exécution un `Call<E>`, représentant l’appel fait par Retrofit, et retourne en sortie le type `E` demandé.

La partie commune à toutes les requêtes comme la vérification du résultat de sortie est mise dans cette `AsyncTask`.
