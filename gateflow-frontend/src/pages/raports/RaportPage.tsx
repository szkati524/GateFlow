import { useState } from "react";
import { useNavigate } from "react-router-dom";
import Header from "../../components/Common/Header";
import styles from './RaportPage.module.css';

const ReportsPage = () => {
    const navigate = useNavigate();
    const [isSidebarOpen, setIsSidebarOpen] = useState(false);
    
    const [reportParams, setReportParams] = useState({
        dateFrom: '',
        dateTo: '',
        company: '',
        reportType: 'all' 
    });

    const toggleSidebar = () => setIsSidebarOpen(!isSidebarOpen);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        setReportParams({
            ...reportParams,
            [e.target.name]: e.target.value
        });
    };

    const handleGenerateReport = async (format: 'pdf' | 'excel') => {
        
        console.log(`Generowanie raportu ${format}...`, reportParams);
        alert(`Rozpoczęto generowanie raportu w formacie ${format.toUpperCase()}. Plik zostanie pobrany automatycznie.`);
    };

    const handleLogout = () => {
        localStorage.removeItem("token");
        localStorage.removeItem("user");
        navigate('/login');
    };

    return (
        <div className={styles.wrapper}>
            <Header />

            <div className={styles.topControls}>
                <button className={styles.menuTrigger} onClick={toggleSidebar}>
                    {isSidebarOpen ? '✕' : '☰'}
                </button>
                <div className={styles.rightActions}>
                    <button className={styles.iconBtn}>⚙️</button>
                    <button className={styles.logoutBtn} onClick={handleLogout}>Wyloguj się</button>
                </div>
            </div>

            <aside className={`${styles.sidebar} ${isSidebarOpen ? styles.sidebarOpen : ''}`}>
                <nav className={styles.navMenu}>
                    <a onClick={() => navigate("/")}>Ruch pojazdów</a>
                    <a onClick={() => navigate("/add-entry")}>Dodaj wjazd</a>
                    <a onClick={() => navigate("/search")}>Wyszukaj</a>
                    <a onClick={() => navigate("/reports")} className={styles.activeLink}>Raporty</a>
                </nav>
            </aside>

            <main className={`${styles.content} ${isSidebarOpen ? styles.contentShifted : ''}`}>
                <div className={styles.reportCard}>
                    <h2>Generator Raportów</h2>
                    <p className={styles.description}>Wybierz parametry, aby wygenerować plik gotowy do druku.</p>
                    
                    <div className={styles.reportForm}>
                        <div className={styles.inputGroup}>
                            <label>Typ raportu</label>
                            <select name="reportType" onChange={handleChange} className={styles.selectInput}>
                                <option value="all">Wszystkie wjazdy</option>
                                <option value="weekly">Ostatnie 7 dni</option>
                                <option value="byCompany">Filtruj po firmie</option>
                            </select>
                        </div>

                        <div className={styles.dateRow}>
                            <div className={styles.inputGroup}>
                                <label>Data od</label>
                                <input type="date" name="dateFrom" onChange={handleChange} />
                            </div>
                            <div className={styles.inputGroup}>
                                <label>Data do</label>
                                <input type="date" name="dateTo" onChange={handleChange} />
                            </div>
                        </div>

                        <div className={styles.inputGroup}>
                            <label>Firma (opcjonalnie)</label>
                            <input type="text" name="company" placeholder="Nazwa firmy" onChange={handleChange} />
                        </div>

                        <div className={styles.buttonGroup}>
                            <button 
                                onClick={() => handleGenerateReport('pdf')} 
                                className={`${styles.actionBtn} ${styles.pdfBtn}`}
                            >
                                📄 Pobierz PDF (Druk)
                            </button>
                            <button 
                                onClick={() => handleGenerateReport('excel')} 
                                className={`${styles.actionBtn} ${styles.excelBtn}`}
                            >
                                📊 Pobierz Excel
                            </button>
                        </div>
                    </div>
                </div>
            </main>
        </div>
    );
};

export default ReportsPage;