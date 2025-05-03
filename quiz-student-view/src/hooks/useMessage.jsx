import { useState, useCallback } from "react";
import { Snackbar, Alert } from "@mui/material";

export default function useMessage() {
  const [messageState, setMessageState] = useState({
    open: false,
    message: "",
    severity: "info",
  });

  const handleMessage = useCallback((message, severity = "info") => {
    setMessageState({ open: true, message, severity });
  }, []);

  const MessageSnackbar = () => (
    <Snackbar
      open={messageState.open}
      autoHideDuration={1000}
      onClose={() => setMessageState((prev) => ({ ...prev, open: false }))}
      anchorOrigin={{ vertical: "bottom", horizontal: "center" }}
    >
      <Alert
        onClose={() => setMessageState((prev) => ({ ...prev, open: false }))}
        severity={messageState.severity}
        sx={{ width: "100%" }}
      >
        {messageState.message}
      </Alert>
    </Snackbar>
  );

  return { handleMessage, MessageSnackbar };
}
