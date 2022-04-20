Version java 17

Lancer le programme

1/ mvn clean install (lance les tests) 
2/ java -jar target/vrecomsys-poc-1.0-SNAPSHOT.jar


8 endpoints (Content-Type = application/json) : 

POST http://localhost:8080/videos - crée une vidéo

exemples de BODY :

  {
    "title": "matrix",
    "labels": [
      "sci-fi",
      "dystopia"
    ],
    "director": "director",
    "release_date": "1982-03-18T12:00:00Z"
  }
  
  {
    "title": "matrix serie",
    "labels": [
      "sci-fi",
      "dystopia"
    ],
    "number_of_episodes": 2
  }
  
  {
    "title": "matrix serie",
    "labels": [
      "sci-fi",
      "dystopia"
    ]
  }
 
 
=> l'id généré est présent dans Location (Header)

GET http://localhost:8080/videos - retourne toutes les vidéos
GET http://localhost:8080/videos?titre=ind - retourne toutes les vidéos dont le titre contient "ind"
GET http://localhost:8080/videos/id - retourne la vidéo avec l'id correspondant
DELETE http://localhost:8080/videos/id - supprime la vidéo avec l'id correspondant
GET http://localhost:8080/videos/historique-suppression - récupère la liste des id supprimés
GET http://localhost:8080/videos/films - récupère tous les films
GET http://localhost:8080/videos/series - récupère les séries
GET http://localhost:8080/videos/id/videos-similaires?nombre_de_label_minimum_en_commun=2 - récupère les vidéos similaires (tags en communs)




  
