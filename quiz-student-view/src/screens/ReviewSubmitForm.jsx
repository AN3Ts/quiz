import { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import {
  Typography,
  Box,
  TextField,
  Button,
  Radio,
  RadioGroup,
  FormControlLabel,
  FormLabel,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
} from "@mui/material";

const ReviewSubmitForm = ({ onReviewSubmitted }) => {
  const { id } = useParams();
  const navigate = useNavigate();

  const [quizName, setQuizName] = useState("");
  const [content, setContent] = useState("");
  const [nickname, setNickname] = useState("");
  const [rating, setRating] = useState(1);
  const [error, setError] = useState("");
  const [showDialog, setShowDialog] = useState(false);

  useEffect(() => {
    const fetchQuizDetails = async () => {
      try {
        const response = await fetch(
          `${import.meta.env.VITE_API_URL}quizzes/${id}`
        );
        if (response.ok) {
          const data = await response.json();
          setQuizName(data.name);
        }
      } catch (err) {
        console.error("Error fetching quiz details:", err);
      }
    };

    fetchQuizDetails();
  }, [id]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!content || !nickname || rating < 1 || rating > 5) {
      setError("Please fill in all fields and provide a valid rating.");
      return;
    }

    try {
      const response = await fetch(
        `${import.meta.env.VITE_API_URL}quizzes/${id}/reviews`,
        {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ content, nickname, rating }),
        }
      );

      if (response.status === 201 || response.ok) {
        setContent("");
        setNickname("");
        setRating(1);
        setError("");
        if (onReviewSubmitted) await onReviewSubmitted();
        setShowDialog(true);
      } else {
        const errorText = await response.text();
        console.error("Review submission failed:", errorText);
        setError("Failed to submit the review. Please try again.");
      }
    } catch (err) {
      console.error("Unexpected error:", err);
      setError("An unexpected error occurred. Please try again.");
    }
  };

  const handleDialogClose = () => {
    setShowDialog(false);
    navigate(`/quizzes/${id}/reviews`);
  };

  return (
    <Box sx={{ maxWidth: 600, margin: "0 auto", padding: 3 }}>
      <Typography
        variant="h4"
        gutterBottom
        sx={{ fontWeight: "bold", color: "#1976d2" }}
      >
        Write a Review for {quizName || "Loading..."}
      </Typography>

      {error && (
        <Typography color="error" sx={{ marginBottom: 2 }}>
          {error}
        </Typography>
      )}

      <form onSubmit={handleSubmit}>
        <TextField
          label="Nickname"
          variant="outlined"
          fullWidth
          value={nickname}
          onChange={(e) => setNickname(e.target.value)}
          required
          sx={{ marginBottom: 2 }}
        />
        <TextField
          label="Review"
          variant="outlined"
          fullWidth
          multiline
          rows={4}
          value={content}
          onChange={(e) => setContent(e.target.value)}
          required
          sx={{ marginBottom: 2 }}
        />
        <FormLabel component="legend" sx={{ marginBottom: 1 }}>
          Rating
        </FormLabel>
        <RadioGroup
          row
          value={rating}
          onChange={(e) => setRating(Number(e.target.value))}
          sx={{ marginBottom: 2 }}
        >
          {[1, 2, 3, 4, 5].map((value) => (
            <FormControlLabel
              key={value}
              value={value}
              control={<Radio />}
              label={value}
            />
          ))}
        </RadioGroup>
        <Button
          type="submit"
          variant="contained"
          color="primary"
          fullWidth
          sx={{ padding: 1 }}
        >
          Submit Review
        </Button>
      </form>

      <Dialog open={showDialog} onClose={handleDialogClose}>
        <DialogTitle>Thank You!</DialogTitle>
        <DialogContent>
          <Typography>Your review has been submitted successfully.</Typography>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleDialogClose} autoFocus>
            OK
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
};

export default ReviewSubmitForm;
