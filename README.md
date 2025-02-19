# Task Manager with Autocomplete Search  
A simple **Task Manager** in Java with an **autocomplete-powered search engine**  

## **Features**  
✅ **Add & Remove Tasks** – Manage your task list easily, (later on)  
✅ **Fast Search with Autocomplete** – Find tasks by typing partial words (e.g., `"gro"` finds `"Grocery shopping"`).  
✅ **Fuzzy Search** – Handles typos (e.g., `"gymm"` finds `"Go to the gym"`).  
✅ **Efficient Indexing** – Uses an **inverted index** for fast lookups.  
✅ **Phrase Matching** – Supports exact phrase searches (e.g., "java programming")  
✅ **Relevance Ranking** – Results sorted by match quality  
✅ **Thread-Safe** – Uses ConcurrentHashMap for safe concurrent access  
✅ **"Did you mean?"** – Suggests corrections for misspelled searches  

## **How It Works**  
- Tasks are stored with an **inverted index** for **quick lookups**  
- Uses **bi-gram indexing** for exact phrase matching  
- The search engine uses a three-tier search strategy:
  1. Exact phrase matching
  2. Individual token matching with prefix support
  3. Fuzzy matching for typos
- Uses **Levenshtein distance** to handle minor typos (up to 2 characters)
- **Relevance scoring** based on exact and prefix matches
- **Normalized tokens** for case-insensitive and special character-free searching

## **Installation & Usage**
1. **Clone the repository:**  
2. **Compile the project:**  
   ```sh
   javac -d out src/org/bunnys/**/*.java
   ```
3. **Run the program:**  
   ```sh
   java -cp out org.bunnys.Main
   ```

## **Example Usage**  
```
Enhanced Task Search System
Try searching with:
- Single words (e.g., 'java')
- Phrases (e.g., 'java programming')
- Partial words (e.g., 'prog')
- Misspelled words (e.g., 'jva')

Enter search query (or 'exit' to quit): java prog
Search results (ranked by relevance):
1. Java programming practice
   Description: Working on Spring Boot

2. Advanced Java programming
   Description: Learning basics

Enter search query (or 'exit' to quit): gymm
No exact matches found. Did you mean: "Go to the gym"?
Would you like to search for this instead? (y/n):
```

## **Project Structure**
```
org/bunnys
├── Main.java                  # Main program entry point
├── task/
│   ├── Task.java             # Task class
│   ├── manager/
│   │   ├── TaskManager.java  # Task manager with search functionality
│   ├── utils/
│       ├── Snowflake.java    # Unique ID generator
│       ├── StringUtils.java  # String utilities (Levenshtein distance)
```

## **Technical Details**
- **Inverted Index**: Maps words to tasks for O(1) lookups
- **Bi-gram Indexing**: Enables phrase matching
- **Thread Safety**: ConcurrentHashMap for concurrent operations
- **Token Normalization**: Case-insensitive, special character handling
- **Relevance Calculation**: 
  - Exact match: 2 points
  - Prefix match: 1 point
  - Results sorted by total relevance score

## **Why This Project?**  
This is a **practice project** for building a **search engine-like experience**, improving **data structures**, and optimizing **search performance**.  

💻 **Built with:** Java, ConcurrentHashMap, Inverted Index, String Matching Algorithms  

## **Future Enhancements**  
🔹 Implement Trie-based search for **faster autocomplete**  
🔹 Add tag-based searching  
🔹 Add search within task descriptions  
🔹 Add time-based relevance  
🔹 Add category filtering  
🔹 Add a **GUI interface** for better user experience  
🔹 Support for regular expressions in search  

---
### ⭐ **Contributions & Feedback**
Feel free to **fork, contribute, or open issues**
