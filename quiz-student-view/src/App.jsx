import { AppBar, Typography, Box, Toolbar, Button } from "@mui/material";
import { BrowserRouter as Router, Routes, Route, Link } from "react-router-dom";
import "./App.css";
import Categories from "./screens/Categories";
import Quizzes from "./screens/Quizzes";
import Questions from "./screens/Questions";
import CategoryQuizzes from "./screens/CategoryQuizzes";
import Results from "./screens/Results";

function App() {
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
            <Button
              color="inherit"
              component={Link}
              to="/"
              sx={{
                "&:hover": {
                  color: "#ffffff",
                },
              }}
            >
              Quizzes
            </Button>
            <Button
              color="inherit"
              component={Link}
              to="/categories"
              sx={{
                "&:hover": {
                  color: "#ffffff",
                },
              }}
            >
              Categories
            </Button>
          </Toolbar>
        </AppBar>

        {/* Main Content */}
        <Box sx={{ padding: 3, flexGrow: 1 }}>
          <Routes>
            <Route path="/" element={<Quizzes />} />
            <Route path="/categories" element={<Categories />} />
            <Route path="/questions/:id" element={<Questions />} />
            <Route path="/categories/:id" element={<CategoryQuizzes />} />
            <Route path="/quizzes/:id/result" element={<Results />} />
          </Routes>
        </Box>
      </Box>
    </Router>
  );
}

export default App;
