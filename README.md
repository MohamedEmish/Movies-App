  
# Movie App
Mohamed Emish
## Features

- Retrieve list of movies from the moviesDB and show them in a grid.
- See more information about the movie in a new screen.
- Mark a movie as a favorite.
- Sort the movies by popularity or rate, and filter by favorite

## Tech

- Kotlin
- Room
- Retrofit
- Modularization
- Constraint layout
- MVVM
- Glide
- Coroutines
- Mock
- Hilt

## Architecture

The architecture used in this project is MVVM.
We will instantiate the usecases in the VM, where the UI will be listening livedatas in case a livedata value is changed, each usecase will provide us a result from a single feature. The repositories will be handling the source where the data is provided (although in this project we won't be switching from local to remote in any feature). The datasource will access the framework to retrieve the response.


## Modularization

The modules are separated by layers, respecting SOLID: 

- App Module houses the activites, in a bigger project we could create a module for each feature

- Presentation Module is responsible for exposing data wrapped in entities that users observe via a LiveData in the ViewModel class.

- Domain Module: This has the Domain Module which contains the usecase responsible for enclosing a particular task, repository interfaces, and entities. 

- Data Layer: This includes the Data Module which is responsible for providing a single source of data. It implements the repository interface defined in the domain layer.

- Core module: This is divided in two submodules: One for dependency injection and other for the setup of room and api.

- Common module: In this module we will house some utils that will be useful in the whole project




