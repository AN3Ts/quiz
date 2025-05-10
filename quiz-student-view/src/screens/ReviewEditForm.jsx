import React, { useState, useEffect } from "react";
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

const ReviewEditForm = ({ onEditSubmitted }) => {
  const { id } = useParams();
  const navigate = useNavigate();

  const [content, setContent] = useState("");
  const [nickname, setNickname] = useState("");
  const [rating, setRating] = useState(1);
  const [showDialog, setShowDialog] = useState(false);
  const [error, setError]= useState(''); 

  useEffect(() => {
    const fetchReviewById = async () => {
      try {
        const response = await fetch(
          `${import.meta.env.VITE_API_URL}reviews/${id}`
        );
        
        if (response.ok) {
          const data = await response.json();
          setContent(data.content); 
          setNickname(data.nickname); 
          setRating(data.rating); 
        }
      } catch (err) {
        console.error("Error fetching review: ", err);
      }
    };

    fetchReviewById();
  }, [id]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    try {
      const response = await fetch(
        `${import.meta.env.VITE_API_URL}reviews/${id}`,
        {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ content, rating }),
        }
      );

      if (response.status === 201 || response.ok) {
        if (onEditSubmitted) await onEditSubmitted();
        setShowDialog(true);
      } else {
        const errorText = await response.text();
        console.error("Edir submission failed:", errorText);
        setError("Failed to edit the review. Please try again.");
      }
    } catch (err) {
      console.error("Unexpected error:", err);
      setError("An unexpected error occurred. Please try again.");
    }
  };

  const handleDialogClose = () => {
    setShowDialog(false);
    navigate(-1); // Go back to the previous page
  };

  return (
    <Box sx={{ maxWidth: 600, margin: "0 auto", padding: 3 }}>
      <Typography
        variant="h4"
        gutterBottom
        sx={{ fontWeight: "bold", color: "#1976d2" }}
      >
        Edit the review by {nickname}: 
      </Typography>

      {error && (
        <Typography color="error" sx={{ marginBottom: 2 }}>
          {error}
        </Typography>
      )}

      <form onSubmit={handleSubmit}>
        
        <TextField
          label="Review"
          variant="outlined"
          fullWidth
          multiline
          rows={4}
          value={content}
          //contentEditable={true}
          onChange={(e) => setContent(e.target.value)}
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
          Edit Review
        </Button>
      </form>

      <Dialog open={showDialog} onClose={handleDialogClose}>
        <DialogTitle>Thank You!</DialogTitle>
        <DialogContent>
          <Typography>Your review has been edited successfully.</Typography>
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

export default ReviewEditForm;
