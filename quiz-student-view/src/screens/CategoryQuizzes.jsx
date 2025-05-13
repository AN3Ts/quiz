import { AgGridReact } from "ag-grid-react";
import { Typography, Box } from "@mui/material";
import useFetchData from "../hooks/useFetchData";
import { useParams } from "react-router-dom";
import dayjs from "dayjs";
import { useNavigate } from "react-router-dom";

export default function CategoryQuizzes() {
  const { id } = useParams();
  const navigate = useNavigate();
  const { data: categoriesQuizzes } = useFetchData(
    import.meta.env.VITE_API_URL + `categories/${id}/quizzes`
  );

  const colDefs = [
    {
      headerName: "Name",
      field: "name",
      filter: "agTextColumnFilter",
      cellRenderer: (param) => {
        return (
          <a
            href="#"
            onClick={(e) => {
              e.preventDefault();
              navigate(`/questions/${param.data.id}`);
            }}
            style={{ textDecoration: "underline", color: "#1976d2" }}
          >
            {param.value}
          </a>
        );
      },
    },
    {
      headerName: "Description",
      field: "description",
      filter: "agTextColumnFilter",
    },

    {
      headerName: "Course",
      field: "courseCode",
      filter: "agTextColumnFilter",
    },
    {
      headerName: "Added on",
      field: "createdDate",
      filter: "agTextColumnFilter",
      valueFormatter: (params) => {
        return params.value ? dayjs(params.value).format("DD.MM.YYYY") : "";
      },
    },
  ];

  return (
    <Box sx={{ padding: 3 }}>
      <Typography
        variant="h4"
        sx={{ fontWeight: "bold", color: "#1976d2", letterSpacing: "1px" }}
      >
        {categoriesQuizzes?.[0]?.category?.name ?? "Category"}
      </Typography>
      <Typography variant="h6">
        {categoriesQuizzes?.[0]?.category?.description ?? "Category"}
      </Typography>
      <div
        className="ag-theme-alpine"
        style={{ height: 300, width: "100%", marginTop: 10 }}
      >
        <AgGridReact
          rowData={categoriesQuizzes}
          columnDefs={colDefs}
          defaultColDef={{ flex: 1, resizable: true }}
          suppressCellFocus={true}
        />
      </div>
    </Box>
  );
}
