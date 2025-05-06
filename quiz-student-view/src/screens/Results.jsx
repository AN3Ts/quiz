import { useParams } from "react-router-dom";
import useFetchData from "../hooks/useFetchData";
import { Typography, Box } from "@mui/material";
import { AgGridReact } from "ag-grid-react";

export default function Results() {
  const { id } = useParams();
  let totalAnswers = 0;
  let totalQuestions = 0;
  let message = "There is no question to display";

  const { data: quiz } = useFetchData(
    import.meta.env.VITE_API_URL + `quizzes/${id}`
  );

  const { data: results } = useFetchData(
    import.meta.env.VITE_API_URL + `quizzes/${id}/result`
  );

  if (results.length > 0) {
    results.map((result) => {
      totalAnswers += result.totalAnswers;
      totalQuestions++;
    });
    message = `${totalAnswers} answers to ${totalQuestions} questions`;
  }

  const colDefs = [
    {
      headerName: "Question",
      field: "questionText",
    },
    {
      headerName: "Difficulty",
      field: "questionDifficulty",
    },
    {
      headerName: "Total answers",
      field: "totalAnswers",
    },
    {
      headerName: "Correct answer %",
      valueFormatter: (params) => {
        let correctRatio = (
          (params.data.correctAnswers / params.data.totalAnswers) *
          100
        ).toFixed(0);
        return correctRatio;
      },
    },
    {
      headerName: "Correct answers",
      field: "correctAnswers",
    },
    {
      headerName: "Wrong answers",
      field: "wrongAnswers",
    },
  ];

  return (
    <Box sx={{ padding: 3 }}>
      <Typography
        variant="h4"
        gutterBottom
        sx={{ fontWeight: "bold", color: "#1976d2", letterSpacing: "1px" }}
      >
        Results of Quiz: {quiz?.name}
      </Typography>
      <Typography variant="h6">{message}</Typography>

      <div
        className="ag-theme-alpine"
        style={{ height: 300, width: "100%", marginTop: 10 }}
      >
        <AgGridReact
          rowData={results}
          columnDefs={colDefs}
          defaultColDef={{ flex: 1, resizable: true }}
          suppressCellFocus={true}
        />
      </div>
    </Box>
  );
}
