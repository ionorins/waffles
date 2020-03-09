# CS126 WAFFLES Coursework Report [1925873]
<!-- This document gives a brief overview about your solution.  -->
<!-- You should change number in the title to your university ID number.  -->
<!-- You should delete these comments  -->
<!-- And for the latter sections should delete and write your explanations in them. -->
<!-- # <-- Indicates heading, ## <-- Indicates subheading, and so on -->

## CustomerStore
### Overview
<!-- A short description about what structures/algorithms you have implemented and why you used them. For example: -->
<!-- The template is only a guide, you are free to make any changes, add any bullets points, re-word it entirely, etc. -->
<!-- * <- is a bullet point, you can also use - minuses or + pluses instead -->
<!-- And this is *italic* and this is **bold** -->
<!-- Words in the grave accents, or in programming terms backticks, formats it as code: `put code here` -->
* I used an AVL tree structure to store and process customers because it is efficient when inserting, deleting and searching.
* I used Quicksort to sort customers by name because it has the lowest possible complexity in terms of time and space.
* I used a hash table for the blacklist because of the constant time both when storing an searching for an ID
  
### Space Complexity
<!-- Write here what you think the overall store space complexity is and gives a brief reason why. -->
<!-- <br> gives a line break -->
<!-- In tables, you don't really need the spaces, it only makes it look nice in text form -->
<!-- You can just do "CustomerStore|O(n)|I have used a single `ArrayList` to store customers." -->
| Store         | Worst Case | Description                                            |
| ------------- | ---------- | ------------------------------------------------------ |
| CustomerStore | O(n)       | Each customer is stored at most once in each structure |

### Time Complexity
<!-- Tell us the time complexity of each method and give a very short description. -->
<!-- These examples may or may not be correct, these examples have not taken account for other requirements like duplicate IDs and such.  -->
<!-- Again, the template is only a guide, you are free to make any changes. -->
<!-- If you did not do a method, enter a dash "-" -->
<!-- Technically, for the getCustomersContaining(s) average case, you are suppose to do it relative to s -->
<!-- Note, that this is using the original convertToAccents() method -->
<!-- So O(a*s + n*(a*t + t)) -->
<!-- Where a is the amount of accents -->
<!-- Where s is the length of the search term String  -->
<!-- Where t is the average length of a name -->
<!-- Where n is the total number of customers -->
<!-- But you can keep it simple -->

| Method                           | Average Case | Description                                                                                             |
| -------------------------------- | ------------ | ------------------------------------------------------------------------------------------------------- |
| addCustomer(Customer c)          | O(logn)      | Tree insert is logn                                                                                     |
| addCustomer(Customer[] c)        | O(nlogn)     | n customers inserted with complexity of logn                                                            |
| getCustomer(Long id)             | O(logn)      | Search in binary tree is logn                                                                           |
| getCustomers()                   | O(n)         | Inorder traversal of tree is n                                                                          |
| getCustomers(Customer[] c)       | O(nlogn)     | Quicksort                                                                                               |
| getCustomersByName()             | O(nlogn)     | Quicksort                                                                                               |
| getCustomersByName(Customer[] c) | O(nlogn)     | Quicksort                                                                                               |
| getCustomersContaining(String s) | O(n*(l+s))   | For each customer (`n`), converts string (`l` - length of string) and searches for the term in it (`s`) |

<!-- Don't delete these <div>s! -->
<!-- And note the spacing, do not change -->
<!-- This is used to get a page break when we convert your report to PDF to read when marking -->
<!-- It is not the end of the world if you do remove, it just makes it harder to read if you do -->
<!-- On things you can remove though, you should remember to remove these comments -->
<div style="page-break-after: always;"></div>

## FavouriteStore
### Overview
* I used an AVL tree structure to store and process favourites because it is efficient when inserting, deleting and searching.
* I used Quicksort for sorting because it has the lowest possible complexity in terms of time and space.
* I used a hash table for the blacklist because of the constant time both when storing an searching for an ID
* I used a hash table to store last date favourited for each customer and restaurant
* I used a hash table of trees to store history of favourites for each customer-restaurant pair
* I used a hash table of trees to store the favourites for each customer
* I used merging algorithms for set operations
* Tops are computed by counting the favourites for each customer/restaurant and sorting the resulting array.
NOTE: A sorting approach is preferable to a linear approach, as the latter would require 20 iterations and nlogn < 20n for n < 2^20 = 1048576.
* Time complexity is given assuming that edge cases occur negligibly rarely

### Space Complexity
| Store          | Worst Case | Description                                                                                              |
| -------------- | ---------- | -------------------------------------------------------------------------------------------------------- |
| FavouriteStore | O(n+c+r)   | Each favourite (`n`), customer (`c`) and restaurant (`r`) are stored at most once in each data structure |

### Time Complexity
| Method                                                          | Average Case | Description                                                     |
| --------------------------------------------------------------- | ------------ | --------------------------------------------------------------- |
| addFavourite(Favourite f)                                       | O(logn)      | Tree insert                                                     |
| addFavourite(Favourite[] f)                                     | O(nlogn)     | n items inserted with logn complexity                           |
| getFavourite(Long id)                                           | O(logn)      | Tree search                                                     |
| getFavourites()                                                 | O(n)         | Inorder traversal                                               |
| getFavourites(Favourite[] f)                                    | O(nlogn)     | Quicksort                                                       |
| getFavouritesByCustomerID(Long id)                              | O(nlogn)     | Linear search and Quicksort                                     |
| getFavouritesByRestaurantID(Long id)                            | O(nlogn)     | Linear search and Quicksort                                     |
| getCommonFavouriteRestaurants(<br>&emsp; Long id1, Long id2)    | O(n)         | Merge algorithm                                                 |
| getMissingFavouriteRestaurants(<br>&emsp; Long id1, Long id2)   | O(n)         | Modified merge algorithm                                        |
| getNotCommonFavouriteRestaurants(<br>&emsp; Long id1, Long id2) | O(n)         | Modified merge algorithm                                        |
| getTopCustomersByFavouriteCount()                               | O(n+clogc)   | Count favourites and sort results (`c` - number of customers)   |
| getTopRestaurantsByFavouriteCount()                             | O(n+rlogr)   | Count favourites and sort results (`r` - number of restaurants) |

<div style="page-break-after: always;"></div>

## RestaurantStore
### Overview
* I used an AVL tree structure to store and process customers because it is efficient when inserting, deleting and searching.
* I used Quicksort to sort customers by name because it has the lowest possible complexity in terms of time and space.
* I used a hash table for the blacklist because of the constant time both when storing an searching for an ID

### Space Complexity
| Store           | Worst Case | Description                                              |
| --------------- | ---------- | -------------------------------------------------------- |
| RestaurantStore | O(n)       | Each restaurant is stored at most once in each structure |

### Time Complexity
| Method                                                                        | Average Case | Description                                                                                               |
| ----------------------------------------------------------------------------- | ------------ | --------------------------------------------------------------------------------------------------------- |
| addRestaurant(Restaurant r)                                                   | O(logn)      | Tree insert                                                                                               |
| addRestaurant(Restaurant[] r)                                                 | O(nlogn)     | n restaurants inserted with complexity of logn                                                            |
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
* I used a hash table for the blacklist because of the constant time both when storing an searching for an ID
* I used a hash table to store last date reviewed for each customer and restaurant
* I used a hash table of trees to store history of reviews for each customer-restaurant pair
* Tops are computed by counting the favourites for each customer/restaurant (or the average rating for each restaurant or apparitions of each keyword) and sorting the resulting array.
NOTE: A sorting approach is preferable to a linear approach, as the latter would require 20 iterations and nlogn < 20n for n < 2^20 = 1048576.
NOTE: For keyword top, as n=82 and the first approach requires 5 iterations, the running times of the two approaches are similar, but the sorting approach was prefered for consistency sake.
* Time complexity is given assuming that edge cases occur negligibly rarely

### Space Complexity
| Store       | Worst Case | Description                                                                                           |
| ----------- | ---------- | ----------------------------------------------------------------------------------------------------- |
| ReviewStore | O(n+c+r)   | Each review (`n`), customer (`c`) and restaurant (`r`) are stored at most once in each data structure |

### Time Complexity
| Method                                                                             | Average Case | Description                                                                                           |
| ---------------------------------------------------------------------------------- | ------------ | ----------------------------------------------------------------------------------------------------- |
| addReview(Review r)                                                                | O(logn)      | Tree insert                                                                                           |
| addReview(Review[] r)                                                              | O(nlogn)     | n reviews inserted with complexity of logn                                                            |
| getReview(Long id)                                                                 | O(logn)      | Tree search                                                                                           |
| getReviews()                                                                       | O(n)         | Inorder traversal                                                                                     |
| getReviews(Review[] r)                                                             | O(nlogn)     | Quicksort                                                                                             |
| getReviewsByDate()                                                                 | O(nlogn)     | Quicksort                                                                                             |
| getReviewsByRating()                                                               | O(nlogn)     | Quicksort                                                                                             |
| getReviewsByRestaurantID(Long id)                                                  | O(nlogn)     | Linear search and Quicksort                                                                           |
| getReviewsByCustomerID(Long id)                                                    | O(nlogn)     | Linear search and Quicksort                                                                           |
| getAverageCustomerReviewRating(Long id)                                            | O(n)         | Linear search                                                                                         |
| getAverageRestaurantReviewRating(Long id)                                          | O(n)         | Linear search                                                                                         |
| getCustomerReviewHistogramCount(Long id)                                           | O(n)         | Linear search                                                                                         |
| getRestaurantReviewHistogramCount(Long id)                                         | O(n)         | Linear search                                                                                         |
| getTopCustomersByReviewCount()                                                     | O(n+clogc)   | Count reviews and sort results (`c` - number of customers)                                            |
| getTopRestaurantsByReviewCount()                                                   | O(n+rlogr)   | Count reviews and sort results (`r` - number of restaurants)                                          |
| getTopRatedRestaurants()                                                           | O(nlogn)     | Linear search and Quicksort                                                                           |
| getTopKeywordsForRestaurant(Long id)                                               | O(n*w+klogk) | For each review(`n`), check each word (`w` - avarage number of words in review),                      |
| and if it is a keyword, increment its value in hash map (`k` - number of keywords) |
| getReviewsContaining(String s)                                                     | O(n*(l+s))   | For each review (`n`), converts string (`l` - length of string) and searches for the term in it (`s`) |  |

<div style="page-break-after: always;"></div>

## Util
### Overview
* **ConvertToPlace**
    * Searches in a hash table with coordinates and places
* **DataChecker**
    * `extractTrueID` checks ids two by two, `isValid(Long inputID)` creates a frequency array with the digits of the id,
      the others just check if the attributes of the object are valid and not null
* **HaversineDistanceCalculator (HaversineDC)**
    * Just applies mathematical formula
* **KeywordChecker**
    * Searches in a hash table of keywords
* **StringFormatter**
    * Interates through each characters, converts it if necessary, appends result to new string

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
