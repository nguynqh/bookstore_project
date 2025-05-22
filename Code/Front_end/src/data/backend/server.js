const express = require('express');
const cors = require('cors');
const userRoutes = require('./userRoutes');
const bookRoutes = require('./bookRoutes');
const app = express();

app.use(cors());
app.use(express.json());
app.use('/api/users', userRoutes);
app.use('/api/books', bookRoutes);

app.listen(5000, () => console.log('Server running on port 5000')); 