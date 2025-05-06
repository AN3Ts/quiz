import useFetchData from "../hooks/useFetchData";
import { AgGridReact } from "ag-grid-react";
import {
  ModuleRegistry,
  ClientSideRowModelModule,
  ValidationModule,
  TextFilterModule,
  PaginationModule,
} from "ag-grid-community";
import { Typography, Box } from "@mui/material";
import "ag-grid-community/styles/ag-theme-alpine.css";
import dayjs from "dayjs";
import "./Quizzes.css";
import { useNavigate } from "react-router-dom";

ModuleRegistry.registerModules([
  ClientSideRowModelModule,
  ValidationModule,
  TextFilterModule,
  PaginationModule,
]);

export default function Quizzes() {
  const { data: quizzes } = useFetchData(
    import.meta.env.VITE_API_URL + "quizzes"
    // import.meta.env.VITE_API_URL_LOCAL + "quizzes"
  );

  const navigate = useNavigate();

  const colDefs = [
    {
      headerName: "Name",
      field: "name",
      sortable: true,
      filter: "agTextColumnFilter",
      cellRenderer: (params) => {
        return (
          <a
            href="#"
            onClick={(e) => {
              e.preventDefault();
              navigate(`/questions/${params.data.id}`);
            }}
            style={{
              color: "#1976d2",
              textDecoration: "underline",
              cursor: "pointer",
            }}
          >
            {params.value}
          </a>
        );
      },
    },
    {
      headerName: "Description",
      field: "description",
      sortable: true,
      filter: "agTextColumnFilter",
    },
    {
      headerName: "Course Code",
      field: "courseCode",
      sortable: true,
      filter: "agTextColumnFilter",
    },
    {
      headerName: "Category",
      field: "category.name",
      sortable: true,
      filter: "agTextColumnFilter",
    },
    {
      headerName: "Added on",
      field: "createdDate",
      sortable: true,
      filter: "agTextColumnFilter",
      valueFormatter: (params) => {
        return params.value ? dayjs(params.value).format("DD.MM.YYYY") : "";
      },
    },
    {
      headerName: "Result",
      cellRenderer: (params) => {
        return (
          <a
            href="#"
            onClick={(e) => {
              e.preventDefault();
              navigate(`quizzes/${params.data.id}/result`);
            }}
            style={{
              color: "#1976d2",
              textDecoration: "underline",
              cursor: "pointer",
            }}
          >
            View results
          </a>
        );
      },
    },
  ];

  return (
    <Box sx={{ padding: 3 }}>
      <Typography
        variant="h4"
        gutterBottom
        sx={{ fontWeight: "bold", color: "#1976d2", letterSpacing: "1px" }}
      >
        Quizzes
      </Typography>
      <div className="ag-theme-alpine" style={{ height: 400, width: "100%" }}>
        <AgGridReact
          rowData={quizzes}
          columnDefs={colDefs}
          defaultColDef={{
            flex: 1,
            resizable: true,
            sortable: true,
            filter: "agTextColumnFilter",
          }}
          pagination={true}
          paginationPageSize={20}
        />
      </div>
    </Box>
  );
}
