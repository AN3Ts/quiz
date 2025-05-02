import useFetchData from "../hooks/useFetchData";
import { AgGridReact } from "ag-grid-react";
import { ModuleRegistry } from "ag-grid-community";
import { ClientSideRowModelModule, ValidationModule } from "ag-grid-community";
import { Typography } from "@mui/material";
import "ag-grid-community/styles/ag-theme-alpine.css";

ModuleRegistry.registerModules([ClientSideRowModelModule, ValidationModule]);

export default function Quizzes() {
  const { data: quizzes } = useFetchData(
    import.meta.env.VITE_API_URL + "quizzes"
  );

  console.log(quizzes);

  const colDefs = [
    { headerName: "Name", field: "name" },
    { headerName: "Description", field: "description" },
    { headerName: "Course Code", field: "courseCode" },
    { headerName: "Category", field: "category.name" },
  ];

  return (
    <div className="quizzes-container">
      <Typography variant="h5">Quizzes</Typography>
      <div
        className="ag-theme-alpine"
        style={{ height: 300, width: "100%", marginTop: 10 }}
      >
        <AgGridReact
          rowData={quizzes}
          columnDefs={colDefs}
          defaultColDef={{ flex: 1, resizable: true }}
        />
      </div>
    </div>
  );
};

