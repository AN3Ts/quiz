import { useParams } from "react-router-dom";
import useFetchData from "../hooks/useFetchData";
import { Typography, Box, Paper } from "@mui/material";
import dayjs from "dayjs";

export default function Questions() {
  const { id } = useParams(); // quiz ID
  const { data: quizzes } = useFetchData(
    import.meta.env.VITE_API_URL + `quizzes/${id}`
    // import.meta.env.VITE_API_URL_LOCAL + `quizzes/${id}`
  );

  // Handle loading or missing data
  if (!quizzes) {
    return (
      <Box sx={{ padding: 3 }}>
        <Typography variant="h5">Loading...</Typography>
      </Box>
    );
  }

  return (
    <Box sx={{ padding: 3 }}>
      <Typography
        variant="h5"
        gutterBottom
        sx={{ fontWeight: "bold", color: "#1976d2", letterSpacing: "1px" }}
      >
        {quizzes?.name}
      </Typography>
      <Typography variant="subtitle1" gutterBottom>
        {quizzes?.description}
      </Typography>
      <Typography variant="subtitle1" gutterBottom>
        Added on:{" "}
        {quizzes?.createdDate
          ? dayjs(quizzes.createdDate).format("DD.MM.YYYY")
          : "N/A"}{" "}
        - Questions: {quizzes?.questions?.length || 0} - Course Code:{" "}
        {quizzes?.courseCode} - Category: {quizzes?.category?.name || "N/A"}
      </Typography>
      {quizzes.questions?.map((q, index) => (
        <Paper
          key={q.id}
          sx={{
            marginTop: 2,
            marginBottom: 2,
            padding: 2,
            backgroundColor: "rgb(239, 245, 250)",
          }}
        >
          <Typography variant="subtitle1" sx={{ fontWeight: "bold" }}>
            {q.questionText}
          </Typography>
          <Typography variant="subtitle1">
            Question {index + 1} of {quizzes.questions.length} - Difficulty:{" "}
            {q.difficulty}
          </Typography>
        </Paper>
      ))}
    </Box>
  );
}
