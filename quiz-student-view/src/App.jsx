import { useEffect, useState } from "react";
import { AppBar, Typography, Box, Toolbar } from "@mui/material";
import "./App.css";

function App() {
  const [quizzes, setQuizzes] = useState([]);

  useEffect(() => {
    const fetchData = async () => {
      const data = await fetch(
        "https://quiz-git-quiz.2.rahtiapp.fi/api/quizzes"
      );
      //const data = await fetch('http://localhost:8080/api/quizzes');
      setQuizzes(await data.json());
    };
    fetchData();
  }, []);

  console.log(quizzes);

  return (
    <Box
      sx={{
        width: "100vw",
        height: "100vh",
        display: "flex",
        flexDirection: "column",
      }}
    >
      <AppBar position="static">
        <Toolbar>
          <Typography
            variant="h6"
            component="div"
            sx={{ flexGrow: 1, fontWeight: "bold" }}
          >
            Quizzer
          </Typography>
        </Toolbar>
      </AppBar>

      <Box sx={{ padding: 3, flexGrow: 1 }}>
        <Typography variant="h5">Available Quizzes</Typography>
        <ul>
          {quizzes.map((quiz) => (
            <li key={quiz.id}>
              <Typography variant="body1">
                <strong>{quiz.name}</strong>: {quiz.description}
              </Typography>
            </li>
          ))}
        </ul>
      </Box>
    </Box>
  );
}

export default App;
