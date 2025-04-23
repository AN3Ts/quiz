import { useEffect, useState } from 'react';
import './App.css';

function App() {
  const [quizzes, setQuizzes] = useState([]);

  useEffect(() => {
    const fetchData = async () => {
      const data = await fetch('https://quiz-git-quiz.2.rahtiapp.fi/api/quizzes');
      const allQuizzes = await data.json();
      setQuizzes(allQuizzes.filter((quiz) => quiz.published === true));
    };
    fetchData();
  }, []);

  console.log(quizzes);

  return (
    <>
      <h1>Student view</h1>
      <div className="card">
        {quizzes.map((quiz) => (
          <li key={quiz.id}>
            {quiz.name}: {quiz.description}
          </li>
        ))}
      </div>
    </>
  );
}

export default App;
