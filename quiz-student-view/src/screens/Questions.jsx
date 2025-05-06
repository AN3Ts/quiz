import { useParams } from "react-router-dom";
import useFetchData from "../hooks/useFetchData";
import {
  Typography,
  Box,
  Paper,
  RadioGroup,
  FormControlLabel,
  Radio,
  Button,
} from "@mui/material";
import dayjs from "dayjs";
import { useState } from "react";
import useMessage from "../hooks/useMessage";

export default function Questions() {
  const { id } = useParams();
  const { data: quizzes } = useFetchData(
    import.meta.env.VITE_API_URL + `quizzes/${id}`
    // import.meta.env.VITE_API_URL_LOCAL + `quizzes/${id}`
  );

  const { handleMessage, MessageSnackbar } = useMessage();

  const [selectedAnswers, setSelectedAnswers] = useState({});

  const handleSelect = (questionId, answerId) => {
    setSelectedAnswers((prev) => ({ ...prev, [questionId]: answerId }));
  };

  const handleSubmit = async (question) => {
    const answerOptionId = selectedAnswers[question.id];

    if (!answerOptionId) {
      handleMessage("Please select an answer.", "warning");
      return;
    }

    try {
      const response = await fetch(
        `${import.meta.env.VITE_API_URL}questions/${question.id}/answers`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({ answerOptionId }),
        }
      );

      if (response.ok) {
        const selectedAnswer = question.answers.find(
          (a) => a.id === answerOptionId
        );
        if (selectedAnswer?.isCorrect) {
          handleMessage("That is correct, good job!", "success");
        } else {
          handleMessage("That is not correct, try again!", "error");
        }
      } else {
        handleMessage("Something went wrong. Please try again.", "warning");
      }
    } catch (error) {
      console.error(error);
      handleMessage("An error occurred. Please try again.", "warning");
    }
  };

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

          <Box sx={{ marginTop: 2 }}>
            <RadioGroup
              value={selectedAnswers[q.id] || ""}
              onChange={(e) => handleSelect(q.id, parseInt(e.target.value))}
            >
              {q.answers?.map((a) => (
                <FormControlLabel
                  key={a.id}
                  value={a.id}
                  control={<Radio />}
                  label={a.answerText}
                />
              ))}
            </RadioGroup>
          </Box>

          <Button
            variant="contained"
            color="primary"
            sx={{ marginTop: 2 }}
            onClick={() => handleSubmit(q)}
          >
            SUBMIT YOUR ANSWER
          </Button>
        </Paper>
      ))}

      <MessageSnackbar />
    </Box>
  );
}
