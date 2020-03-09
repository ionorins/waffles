# CS126 WAFFLES Coursework Report [1925873]

## CustomerStore
### Overview
* I used an AVL tree structure to store and process customers because it is efficient when inserting, deleting and searching.
* I used Quicksort to sort customers by name because it has the lowest possible complexity in terms of time and space.
* I used a hash table for the blacklist because of the constant time both when storing an searching for an ID.

### Space Complexity
| Store         | Worst Case | Description                                            |
| ------------- | ---------- | ------------------------------------------------------ |
| CustomerStore | O(n)       | Each customer is stored at most once in each structure |

### Time Complexity

| Method                           | Average Case | Description                                                                                             |
| -------------------------------- | ------------ | ------------------------------------------------------------------------------------------------------- |
| addCustomer(Customer c)          | O(logn)      | Tree insert is logn                                                                                     |
| addCustomer(Customer[] c)        | O(ilogn)     | `i` items inserted with complexity of logn                                                              |
| getCustomer(Long id)             | O(logn)      | Search in binary tree is logn                                                                           |
| getCustomers()                   | O(n)         | Inorder traversal of tree is n                                                                          |
| getCustomers(Customer[] c)       | O(nlogn)     | Quicksort                                                                                               |
| getCustomersByName()             | O(nlogn)     | Quicksort                                                                                               |
| getCustomersByName(Customer[] c) | O(nlogn)     | Quicksort                                                                                               |
| getCustomersContaining(String s) | O(n*(l+s))   | For each customer (`n`), converts string (`l` - length of string) and searches for the term in it (`s`) |

<div style="page-break-after: always;"></div>

## FavouriteStore
### Overview
* I used an AVL tree structure to store and process favourites because it is efficient when inserting, deleting and searching.
* I used Quicksort for sorting because it has the lowest possible complexity in terms of time and space.
* I used a hash table for the blacklist because of the constant time both when storing an searching for an ID.
* I used a hash table to store last date favourited for each customer and restaurant.
* I used a hash table of trees to store history of favourites for each customer-restaurant pair.
* I used a hash table of trees to store the favourites for each customer.
* I used merging algorithms for set operations.
* Tops are computed by counting the favourites for each customer/restaurant and sorting the resulting array.
NOTE: A sorting approach is preferable to a linear approach, as the latter would require 20 iterations and nlogn < 20n for n < 2^20 = 1048576.
* Time complexities are given assuming that edge cases occur negligibly rarely.

### Space Complexity
| Store          | Worst Case | Description                                                                                             |
| -------------- | ---------- | ------------------------------------------------------------------------------------------------------- |
| FavouriteStore | O(n+c+r)   | Each favourite (`n`), customer (`c`) and restaurant (`r`) is stored at most once in each data structure |

### Time Complexity
| Method                                                          | Average Case | Description                                                     |
| --------------------------------------------------------------- | ------------ | --------------------------------------------------------------- |
| addFavourite(Favourite f)                                       | O(logn)      | Tree insert                                                     |
| addFavourite(Favourite[] f)                                     | O(ilogn)     | `i` items inserted with complexity of logn                      |
| getFavourite(Long id)                                           | O(logn)      | Tree search                                                     |
| getFavourites()                                                 | O(n)         | Inorder traversal                                               |
| getFavourites(Favourite[] f)                                    | O(nlogn)     | Quicksort                                                       |
| getFavouritesByCustomerID(Long id)                              | O(nlogn)     | Linear search and Quicksort                                     |
| getFavouritesByRestaurantID(Long id)                            | O(nlogn)     | Linear search and Quicksort                                     |
| getCommonFavouriteRestaurants(<br>&emsp; Long id1, Long id2)    | O(n)         | Modified merge algorithm                                        |
| getMissingFavouriteRestaurants(<br>&emsp; Long id1, Long id2)   | O(n)         | Modified merge algorithm                                        |
| getNotCommonFavouriteRestaurants(<br>&emsp; Long id1, Long id2) | O(n)         | Modified merge algorithm                                        |
| getTopCustomersByFavouriteCount()                               | O(n+clogc)   | Count favourites and sort results (`c` - number of customers)   |
| getTopRestaurantsByFavouriteCount()                             | O(n+rlogr)   | Count favourites and sort results (`r` - number of restaurants) |

<div style="page-break-after: always;"></div>

## RestaurantStore
### Overview
* I used an AVL tree structure to store and process customers because it is efficient when inserting, deleting and searching.
* I used Quicksort to sort customers by name because it has the lowest possible complexity in terms of time and space.
* I used a hash table for the blacklist because of the constant time both when storing an searching for an ID.

### Space Complexity
| Store           | Worst Case | Description                                              |
| --------------- | ---------- | -------------------------------------------------------- |
| RestaurantStore | O(n)       | Each restaurant is stored at most once in each structure |

### Time Complexity
| Method                                                                        | Average Case | Description                                                                                               |
| ----------------------------------------------------------------------------- | ------------ | --------------------------------------------------------------------------------------------------------- |
| addRestaurant(Restaurant r)                                                   | O(logn)      | Tree insert                                                                                               |
| addRestaurant(Restaurant[] r)                                                 | O(ilogn)     | `i` items inserted with complexity of logn                                                                |
| getRestaurant(Long id)                                                        | O(logn)      | Tree search                                                                                               |
| getRestaurants()                                                              | O(n)         | Inorder traversal                                                                                         |
| getRestaurants(Restaurant[] r)                                                | O(nlogn)     | Quicksort                                                                                                 |
| getRestaurantsByName()                                                        | O(nlogn)     | Quicksort                                                                                                 |
| getRestaurantsByDateEstablished()                                             | O(nlogn)     | Quicksort                                                                                                 |
| getRestaurantsByDateEstablished(<br>&emsp; Restaurant[] r)                    | O(nlogn)     | Quicksort                                                                                                 |
| getRestaurantsByWarwickStars()                                                | O(nlogn)     | Quicksort                                                                                                 |
| getRestaurantsByRating(Restaurant[] r)                                        | O(nlogn)     | Quicksort                                                                                                 |
| getRestaurantsByDistanceFrom(<br>&emsp; float lat, float lon)                 | O(nlogn)     | Quicksort                                                                                                 |
| getRestaurantsByDistanceFrom(<br>&emsp; Restaurant[] r, float lat, float lon) | O(nlogn)     | Quicksort                                                                                                 |
| getRestaurantsContaining(String s)                                            | O(n*(l+s))   | For each restaurant (`n`), converts string (`l` - length of string) and searches for the term in it (`s`) |

<div style="page-break-after: always;"></div>

## ReviewStore
### Overview
* I used an AVL tree structure to store and process reviews because it is efficient when inserting, deleting and searching.
* I used Quicksort for sorting because it has the lowest possible complexity in terms of time and space.
* I used a hash table for the blacklist because of the constant time both when storing an searching for an ID.
* I used a hash table to store last date reviewed for each customer and restaurant.
* I used a hash table of trees to store history of reviews for each customer-restaurant pair.
* Tops are computed by counting the favourites for each customer/restaurant (or the average rating for each restaurant or apparitions of each keyword) and sorting the resulting array.
NOTE: A sorting approach is preferable to a linear approach, as the latter would require 20 iterations and nlogn < 20n for n < 2^20 = 1048576.
NOTE: For keyword top, as n=82 and the first approach requires 5 iterations, the running times of the two approaches are similar, but the sorting approach was prefered for consistency sake.
* Time complexities are given assuming that edge cases occur negligibly rarely.

### Space Complexity
| Store       | Worst Case | Description                                                                                          |
| ----------- | ---------- | ---------------------------------------------------------------------------------------------------- |
| ReviewStore | O(n+c+r)   | Each review (`n`), customer (`c`) and restaurant (`r`) is stored at most once in each data structure |

### Time Complexity
| Method                                     | Average Case | Description                                                                                                                                                        |
| ------------------------------------------ | ------------ | ------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| addReview(Review r)                        | O(logn)      | Tree insert                                                                                                                                                        |
| addReview(Review[] r)                      | O(ilogn)     | `i` items inserted with complexity of logn                                                                                                                         |
| getReview(Long id)                         | O(logn)      | Tree search                                                                                                                                                        |
| getReviews()                               | O(n)         | Inorder traversal                                                                                                                                                  |
| getReviews(Review[] r)                     | O(nlogn)     | Quicksort                                                                                                                                                          |
| getReviewsByDate()                         | O(nlogn)     | Quicksort                                                                                                                                                          |
| getReviewsByRating()                       | O(nlogn)     | Quicksort                                                                                                                                                          |
| getReviewsByRestaurantID(Long id)          | O(nlogn)     | Linear search and Quicksort                                                                                                                                        |
| getReviewsByCustomerID(Long id)            | O(nlogn)     | Linear search and Quicksort                                                                                                                                        |
| getAverageCustomerReviewRating(Long id)    | O(n)         | Linear search                                                                                                                                                      |
| getAverageRestaurantReviewRating(Long id)  | O(n)         | Linear search                                                                                                                                                      |
| getCustomerReviewHistogramCount(Long id)   | O(n)         | Linear search                                                                                                                                                      |
| getRestaurantReviewHistogramCount(Long id) | O(n)         | Linear search                                                                                                                                                      |
| getTopCustomersByReviewCount()             | O(n+clogc)   | Count reviews and sort results (`c` - number of customers)                                                                                                         |
| getTopRestaurantsByReviewCount()           | O(n+rlogr)   | Count reviews and sort results (`r` - number of restaurants)                                                                                                       |
| getTopRatedRestaurants()                   | O(nlogn)     | Linear search and Quicksort                                                                                                                                        |
| getTopKeywordsForRestaurant(Long id)       | O(n*w+klogk) | For each review(`n`), check each word (`w` - avarage number of words in review),and if it is a keyword, increment its value in hash map (`k` - number of keywords) |
| getReviewsContaining(String s)             | O(n*(l+s))   | For each review (`n`), converts string (`l` - length of string) and searches for the term in it (`s`)                                                              |  |

<div style="page-break-after: always;"></div>

## Util
### Overview
* **ConvertToPlace**
    * Searches in a hash table with coordinates and places.
* **DataChecker**
    * `extractTrueID` checks ids two by two, `isValid(Long inputID)` creates a frequency array with the digits of the id,
      the other methods just check if the attributes of the object are valid and not null.
* **HaversineDistanceCalculator (HaversineDC)**
    * Just applies mathematical formula.
* **KeywordChecker**
    * Searches in a hash table of keywords.
* **StringFormatter**
    * Interates through each characters, converts it if necessary using switch statement, appends result to new string.

### Space Complexity
| Util            | Worst Case | Description                                                           |
| --------------- | ---------- | --------------------------------------------------------------------- |
| ConvertToPlace  | O(p)       | Hash table containing coordinates and places (`p` - number of places) |
| DataChecker     | O(1)       | Nothing in particular is stored                                       |
| HaversineDC     | O(1)       | Nothing in particular is stored                                       |
| KeywordChecker  | O(k)       | Hash table containing keywords (`k` - number of keywords)             |
| StringFormatter | O(1)       | Nothing in particular is stored                                       |

### Time Complexity
| Util            | Method                                                                             | Average Case | Description                   |
| --------------- | ---------------------------------------------------------------------------------- | ------------ | ----------------------------- |
| ConvertToPlace  | convert(float lat, float lon)                                                      | O(1)         | Hash table look up            |
| DataChecker     | extractTrueID(String[] repeatedID)                                                 | O(1)         | Constant number of operations |
| DataChecker     | isValid(Long id)                                                                   | O(1)         | Constant number of operations |
| DataChecker     | isValid(Customer customer)                                                         | O(1)         | Constant number of operations |
| DataChecker     | isValid(Favourite favourite)                                                       | O(1)         | Constant number of operations |
| DataChecker     | isValid(Restaurant restaurant)                                                     | O(1)         | Constant number of operations |
| DataChecker     | isValid(Review review)                                                             | O(1)         | Constant number of operations |
| HaversineDC     | inKilometres(<br>&emsp; float lat1, float lon1, <br>&emsp; float lat2, float lon2) | O(1)         | Applies formula               |
| HaversineDC     | inMiles(<br>&emsp; float lat1, float lon1, <br>&emsp; float lat2, float lon2)      | O(1)         | Applies formula               |
| KeywordChecker  | isAKeyword(String s)                                                               | O(1)         | Hash table look up            |
| StringFormatter | convertAccentsFaster(String s)                                                     | O(l)         | `l` - length of string        |
