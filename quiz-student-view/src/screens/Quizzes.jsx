import useFetchData from "../hooks/useFetchData";
import { AgGridReact } from "ag-grid-react";
import { ModuleRegistry } from "ag-grid-community";
import { ClientSideRowModelModule, ValidationModule } from "ag-grid-community";
import { Typography, Box, Paper } from "@mui/material";
import "ag-grid-community/styles/ag-theme-alpine.css";
import dayjs from "dayjs";
import "./Quizzes.css";

// Register AG Grid modules
ModuleRegistry.registerModules([ClientSideRowModelModule, ValidationModule]);

export default function Quizzes() {
  const { data: quizzes } = useFetchData(
    import.meta.env.VITE_API_URL + "quizzes"
  );

  console.log(quizzes);

  const colDefs = [
    {
      headerName: "Name",
      field: "name",
      sortable: true,
      filter: "agTextColumnFilter",
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
        // Format the date using Day.js
        return params.value ? dayjs(params.value).format("DD.MM.YYYY") : "";
      },
    },
  ];

  return (
    <Box sx={{ padding: 3 }}>
      <Typography
        variant="h4"
        gutterBottom
        style={{ fontWeight: "bold", color: "#1976d2", letterSpacing:"1px" }}
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
          paginationPageSize={10}
        />
      </div>
    </Box>
  );
}
