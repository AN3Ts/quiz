import { useEffect, useState } from "react";
import { AppBar, Typography, Box, Toolbar, Button } from "@mui/material";
import { BrowserRouter as Router, Routes, Route, Link } from "react-router-dom";
import "./App.css";
import Categories from "./components/Categories";

// Placeholder components for Quizz and Categories
function Quizz() {
  return <Typography variant="h5">Welcome to Quizz</Typography>;
}

function App() {
  const [quizzes, setQuizzes] = useState([]);

  useEffect(() => {
    const fetchData = async () => {
      const data = await fetch(
        "https://quiz-git-quiz.2.rahtiapp.fi/api/quizzes"
      );
      setQuizzes(await data.json());
    };
    fetchData();
  }, []);

  return (
    <Router>
      <Box
        sx={{
          width: "100vw",
          height: "100vh",
          display: "flex",
          flexDirection: "column",
        }}
      >
        {/* AppBar with navigation links */}
        <AppBar position="static">
          <Toolbar>
            <Typography
              variant="h6"
              component="div"
              sx={{ flexGrow: 1, fontWeight: "bold" }}
            >
              Quizzer
            </Typography>
            <Button color="inherit" component={Link} to="/">
              Quizz
            </Button>
            <Button color="inherit" component={Link} to="/categories">
              Categories
            </Button>
          </Toolbar>
        </AppBar>

        {/* Main Content */}
        <Box sx={{ padding: 3, flexGrow: 1 }}>
          <Routes>
            <Route path="/" element={<Quizz />} />
            <Route path="/categories" element={<Categories />} />
          </Routes>
        </Box>
      </Box>
    </Router>
  );
}

export default App;
