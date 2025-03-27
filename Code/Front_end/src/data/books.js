const books = [
  {
    id: 1,
    title: "To Kill a Mockingbird",
    author: "Harper Lee",
    category: "Fiction",
    price: 200000,
    stock: 25,
    image: "https://images-na.ssl-images-amazon.com/images/I/81aY1lxk+9L.jpg",
    description: "To Kill a Mockingbird is a novel by Harper Lee published in 1960. It was immediately successful, winning the Pulitzer Prize, and has become a classic of modern American literature. The plot and characters are loosely based on Lee's observations of her family, her neighbors and an event that occurred near her hometown of Monroeville, Alabama, in 1936, when she was 10 years old."
  },
  {
    id: 2,
    title: "1984",
    author: "George Orwell",
    category: "Science Fiction",
    price: 100000,
    stock: 18,
    image: "https://images-na.ssl-images-amazon.com/images/I/71kxa1-0mfL.jpg",
    description: "1984 is a dystopian novel by English novelist George Orwell. It was published in 1949 as Orwell's ninth and final book completed in his lifetime. The story was mostly written at Barnhill, a farmhouse on the Scottish island of Jura, at times while Orwell suffered from severe tuberculosis."
  },
  {
    id: 3,
    title: "The Great Gatsby",
    author: "F. Scott Fitzgerald",
    category: "Fiction",
    price: 213000,
    stock: 15,
    image: "https://images-na.ssl-images-amazon.com/images/I/71FTb9X6wsL.jpg",
    description: "The Great Gatsby is a 1925 novel by American writer F. Scott Fitzgerald. Set in the Jazz Age on Long Island, the novel depicts narrator Nick Carraway's interactions with mysterious millionaire Jay Gatsby and Gatsby's obsession to reunite with his former lover, Daisy Buchanan."
  },
  {
    id: 4,
    title: "Pride and Prejudice",
    author: "Jane Austen",
    category: "Romance",
    price: 213000,
    stock: 20,
    image: "https://images-na.ssl-images-amazon.com/images/I/71Q1tPupKjL.jpg",
    description: "Pride and Prejudice is an 1813 romantic novel of manners written by Jane Austen. The novel follows the character development of Elizabeth Bennet, the dynamic protagonist of the book who learns about the repercussions of hasty judgments and comes to appreciate the difference between superficial goodness and actual goodness."
  },
  {
    id: 5,
    title: "Atomic Habits",
    author: "James Clear",
    category: "Self-help",
    price: 213000,
    stock: 30,
    image: "https://images-na.ssl-images-amazon.com/images/I/81wgcld4wxL.jpg",
    description: "A supremely practical and useful book. James Clear distills the most fundamental information about habit formation, so you can accomplish more by focusing on less."
  },
  {
    id: 6,
    title: "Dune",
    author: "Frank Herbert",
    category: "Science Fiction",
    price: 213000,
    stock: 12,
    image: "https://images-na.ssl-images-amazon.com/images/I/81ym3QUd3KL.jpg",
    description: "Dune is a 1965 science fiction novel by American author Frank Herbert. It is the first installment of the Dune saga. Set in the distant future amidst a feudal interstellar society in which various noble houses control planetary fiefs, Dune tells the story of young Paul Atreides."
  },
  {
    id: 7,
    title: "The Hobbit",
    author: "J.R.R. Tolkien",
    category: "Fantasy",
    price: 213000,
    stock: 22,
    image: "https://images-na.ssl-images-amazon.com/images/I/91b0C2YNSrL.jpg",
    description: "The Hobbit is set within Tolkien's fictional universe and follows the quest of home-loving Bilbo Baggins, the titular hobbit, to win a share of the treasure guarded by Smaug the dragon."
  },
  {
    id: 8,
    title: "Harry Potter and the Philosopher's Stone",
    author: "J.K. Rowling",
    category: "Fantasy",
    price: 335000,
    stock: 35,
    image: "https://images-na.ssl-images-amazon.com/images/I/81iqZ2HHD-L.jpg",
    description: "The first novel in the Harry Potter series follows Harry Potter, a young wizard who discovers his magical heritage on his eleventh birthday."
  },
  {
    id: 9,
    title: "The Alchemist",
    author: "Paulo Coelho",
    category: "Fiction",
    price: 240000,
    stock: 28,
    image: "https://images-na.ssl-images-amazon.com/images/I/71aFt4+OTOL.jpg",
    description: "The Alchemist follows the journey of an Andalusian shepherd boy named Santiago who dreams of finding worldly treasures."
  },
  {
    id: 10,
    title: "The Da Vinci Code",
    author: "Dan Brown",
    category: "Mystery",
    price: 312000,
    stock: 25,
    image: "https://images-na.ssl-images-amazon.com/images/I/81C+0xB9UjL.jpg",
    description: "The Da Vinci Code follows symbologist Robert Langdon and cryptologist Sophie Neveu as they investigate a murder in Paris's Louvre Museum."
  },
  {
    id: 11,
    title: "Clean Architecture",
    author: "Robert C. Martin",
    category: "Programming",
    price: 912000,
    stock: 10,
    image: "https://images-na.ssl-images-amazon.com/images/I/41-sN-mzwKL.jpg",
    description: "A comprehensive guide to software structure and design by the renowned software engineer Robert C. Martin."
  },
  {
    id: 12,
    title: "The Catcher in the Rye",
    author: "J.D. Salinger",
    category: "Fiction",
    price: 216000,
    stock: 20,
    image: "https://images-na.ssl-images-amazon.com/images/I/81OthjkJBuL.jpg",
    description: "The story of Holden Caulfield, a teenage boy who has been expelled from prep school and is wandering around New York City."
  },
  {
    id: 13,
    title: "The Lord of the Rings",
    author: "J.R.R. Tolkien",
    category: "Fantasy",
    price: 480000,
    stock: 15,
    image: "https://images-na.ssl-images-amazon.com/images/I/71jLBXtWJWL.jpg",
    description: "An epic high-fantasy novel about the quest to destroy the One Ring, an artifact that helps the Dark Lord Sauron conquer Middle-earth."
  },
  {
    id: 14,
    title: "Becoming",
    author: "Michelle Obama",
    category: "Biography",
    price: 360000,
    stock: 18,
    image: "https://images-na.ssl-images-amazon.com/images/I/81dDwAzxtrL.jpg",
    description: "In her memoir, Michelle Obama chronicles the experiences that have shaped her—from her childhood to her years as an executive and her time at the White House."
  },
  {
    id: 15,
    title: "Educated",
    author: "Tara Westover",
    category: "Memoir",
    price: 335000,
    stock: 22,
    image: "https://images-na.ssl-images-amazon.com/images/I/81NwOj14S6L.jpg",
    description: "A memoir about a woman who leaves her survivalist family and goes on to earn a PhD from Cambridge University."
  },
  {
    id: 16,
    title: "Sapiens",
    author: "Yuval Noah Harari",
    category: "History",
    price: 408000,
    stock: 25,
    image: "https://images-na.ssl-images-amazon.com/images/I/71cxfzWv9vL.jpg",
    description: "A brief history of humankind, detailing the development and impact of key revolutions in human history."
  },
  {
    id: 17,
    title: "JavaScript: The Good Parts",
    author: "Douglas Crockford",
    category: "Programming",
    price: 720000,
    stock: 14,
    image: "https://images-na.ssl-images-amazon.com/images/I/81kqrwS1nNL.jpg",
    description: "This book scrapes away these bad features to reveal a subset of JavaScript that's more reliable, readable, and maintainable."
  },
  {
    id: 18,
    title: "The Road",
    author: "Cormac McCarthy",
    category: "Fiction",
    price: 264000,
    stock: 18,
    image: "https://images-na.ssl-images-amazon.com/images/I/71IJ1HC2a3L.jpg",
    description: "A post-apocalyptic novel about a journey of a father and his young son over a period of several months across a landscape blasted by an unspecified cataclysm."
  },
  {
    id: 19,
    title: "Design Patterns",
    author: "Erich Gamma",
    category: "Programming",
    price: 1032000,
    stock: 8,
    image: "https://images-na.ssl-images-amazon.com/images/I/51szD9HC9pL.jpg",
    description: "A book that has been influential to the field of software engineering and is regarded as an important source for object-oriented design theory and practice."
  },
  {
    id: 20,
    title: "Little Women",
    author: "Louisa May Alcott",
    category: "Fiction",
    price: 192000,
    stock: 30,
    image: "https://images-na.ssl-images-amazon.com/images/I/91oBK8CuElL.jpg",
    description: "The novel follows the lives of the four March sisters—Meg, Jo, Beth, and Amy—and details their passage from childhood to womanhood."
  },
  {
    id: 21,
    title: "The Hunger Games",
    author: "Suzanne Collins",
    category: "Young Adult",
    price: 288000,
    stock: 27,
    image: "https://images-na.ssl-images-amazon.com/images/I/61JfGcL2ljL.jpg",
    description: "A dystopian novel where teenagers are selected to compete in a televised battle to the death."
  },
  {
    id: 22,
    title: "Thinking, Fast and Slow",
    author: "Daniel Kahneman",
    category: "Psychology",
    price: 384000,
    stock: 19,
    image: "https://images-na.ssl-images-amazon.com/images/I/71wvKXWfcIL.jpg",
    description: "The book summarizes research that Kahneman conducted over decades, often in collaboration with Amos Tversky. It covers all three phases of his career."
  },
  {
    id: 23,
    title: "Crime and Punishment",
    author: "Fyodor Dostoevsky",
    category: "Fiction",
    price: 240000,
    stock: 16,
    image: "https://images-na.ssl-images-amazon.com/images/I/81VcgUQMIKL.jpg",
    description: "A novel that focuses on the mental anguish and moral dilemmas of Rodion Raskolnikov, an impoverished ex-student in Saint Petersburg who formulates a plan to kill a pawnbroker."
  },
  {
    id: 24,
    title: "The Lean Startup",
    author: "Eric Ries",
    category: "Business",
    price: 480000,
    stock: 22,
    image: "https://images-na.ssl-images-amazon.com/images/I/81-QB7nDh4L.jpg",
    description: "A methodology for developing businesses and products that aims to shorten product development cycles."
  },
  {
    id: 25,
    title: "Brave New World",
    author: "Aldous Huxley",
    category: "Science Fiction",
    price: 264000,
    stock: 24,
    image: "https://images-na.ssl-images-amazon.com/images/I/81zE42gT3xL.jpg",
    description: "A dystopian social science fiction novel that depicts a genetically-engineered future where life is pain-free but meaningless."
  },
  {
    id: 26,
    title: "Animal Farm",
    author: "George Orwell",
    category: "Fiction",
    price: 216000,
    stock: 28,
    image: "https://images-na.ssl-images-amazon.com/images/I/71wdbQRdUzL.jpg",
    description: "A satirical allegorical novella that reflects events leading up to the Russian Revolution of 1917 and the early years of the Soviet Union."
  },
  {
    id: 27,
    title: "The Art of War",
    author: "Sun Tzu",
    category: "Philosophy",
    price: 192000,
    stock: 32,
    image: "https://images-na.ssl-images-amazon.com/images/I/71dNsRuYL7L.jpg",
    description: "An ancient Chinese military treatise dating from the Late Spring and Autumn Period. The work contains a detailed explanation of military strategies and tactics."
  },
  {
    id: 28,
    title: "The Fault in Our Stars",
    author: "John Green",
    category: "Young Adult",
    price: 264000,
    stock: 30,
    image: "https://images-na.ssl-images-amazon.com/images/I/81a4kCNuH%2BL.jpg",
    description: "The story follows Hazel Grace Lancaster, a 16-year-old cancer patient who is forced by her parents to attend a support group, where she meets and falls in love with Augustus Waters."
  },
  {
    id: 29,
    title: "The Girl on the Train",
    author: "Paula Hawkins",
    category: "Mystery",
    price: 240000,
    stock: 17,
    image: "https://images-na.ssl-images-amazon.com/images/I/81NcnnhI%2BpL.jpg",
    description: "A psychological thriller novel that debuted at No. 1 on The New York Times Fiction Best Sellers of 2015 list."
  },
  {
    id: 30,
    title: "Gone Girl",
    author: "Gillian Flynn",
    category: "Thriller",
    price: 288000,
    stock: 21,
    image: "https://images-na.ssl-images-amazon.com/images/I/91BCB1KYX4L.jpg",
    description: "A thriller novel about a man whose wife has gone missing in suspicious circumstances, with him becoming the prime suspect."
  }
];

export default books;
