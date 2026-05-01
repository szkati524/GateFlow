import { BrowserRouter as Router,Routes,Route } from "react-router-dom";
import AddEntryPage from "./pages/AddEntry/AddEntryPage";
import MainPage from "./pages/Main/MainPageTemp";
import LoginPage from "./pages/temp_login/LoginPage";
import "./App.css";
import SearchPage from "./pages/SeachPage/SearchPageTemp";
import RaportPage from "./pages/raports/RaportPage";
import Options from "./pages/Options/Options";



function App() {
return (
  <Router>
    <div className="App">
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route path="/add-entry" element={<AddEntryPage />} />
        <Route path="/" element={<MainPage />} />
        <Route path="/search" element={<SearchPage />} />
       <Route path="/raport" element={<RaportPage />} />
       <Route path="/options" element={<Options/>} />
      </Routes>
    </div>
  </Router>
);
}


export default App;
