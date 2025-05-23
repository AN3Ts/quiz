import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import {
  Typography,
  Box,
  Button,
  Card,
  CardContent,
  Grid,
  Rating,
  CardActions,
  IconButton,
} from "@mui/material";
import Edit from "@mui/icons-material/Edit";
import Delete from "@mui/icons-material/Delete";

const Reviews = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [quiz, setQuiz] = useState({});

  const handleFetch = async () => {
    try {
      const res = await fetch(`${import.meta.env.VITE_API_URL}quizzes/${id}`);
      const data = await res.json();
      setQuiz(data);
    } catch (err) {
      console.error("Fetch error:", err);
    }
  };

  useEffect(() => {
    handleFetch();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [id]);

  if (!quiz) {
    return <p>Loading reviews...</p>;
  }

  const handleDelete = async (e, reviewId) => {
    e.preventDefault();
    try {
      const response = await fetch(
        `${import.meta.env.VITE_API_URL}reviews/${reviewId}`,
        {
          method: "DELETE",
          headers: { "Content-Type": "application/json" },
        }
      );

      if (response.status === 200) {
        handleFetch();
      }
    } catch (err) {
      console.error("Delete error: ", err);
    }
  };

  const averageRating =
    quiz.reviews && quiz.reviews.length > 0
      ? quiz.reviews.reduce((sum, r) => sum + r.rating, 0) / quiz.reviews.length
      : 0;

  return (
    <Box sx={{ maxWidth: 900, margin: "0 auto", padding: 3 }}>
      <Typography
        variant="h4"
        gutterBottom
        sx={{ fontWeight: "bold", color: "#1976d2", letterSpacing: "1px" }}
      >
        Reviews for Quiz: {quiz.name}
      </Typography>

      <Box sx={{ display: "flex", alignItems: "center", mb: 2 }}>
        <Typography variant="h6" sx={{ mr: 1 }}>
          Average Rating:
        </Typography>
        <Rating value={averageRating} precision={0.1} readOnly />
        <Typography variant="body2" sx={{ ml: 1 }}>
          ({averageRating.toFixed(1)} / 5)
        </Typography>
      </Box>

      <Button
        onClick={() => navigate(`/quizzes/${id}/submit-review`)}
        variant="outlined"
        sx={{ mb: 3 }}
      >
        Write a Review
      </Button>

      {quiz.reviews?.length === 0 ? (
        <Typography>No reviews yet. Be the first to write one!</Typography>
      ) : (
        <Grid container spacing={2}>
          {quiz.reviews?.map((review) => (
            <Grid key={review.id}>
              <Card sx={{ borderRadius: 2, boxShadow: 3 }}>
                <CardContent>
                  <Typography variant="h6" sx={{ fontWeight: "bold" }}>
                    {review.nickname}
                  </Typography>
                  <Rating
                    value={review.rating}
                    readOnly
                    size="small"
                    sx={{ mb: 1 }}
                  />
                  <Typography variant="body1" sx={{ mb: 1 }}>
                    {review.content}
                  </Typography>
                  <Typography variant="caption" color="text.secondary">
                    {new Date(review.createdDate).toLocaleDateString()}
                  </Typography>
                </CardContent>
                <CardActions disableSpacing>
                  <IconButton
                    aria-label="Delete Review"
                    onClick={(e) => handleDelete(e, review.id)}
                  >
                    <Delete />
                  </IconButton>
                  <IconButton
                    aria-label="Edit Review"
                    onClick={() => navigate(`/reviews/edit/${review.id}`)}
                  >
                    <Edit />
                  </IconButton>
                </CardActions>
              </Card>
            </Grid>
          ))}
        </Grid>
      )}
    </Box>
  );
};

export default Reviews;
