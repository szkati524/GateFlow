import { useState } from "react";
import Navbar from "../../components/Common/Navbar";
import styles from './RaportPage.module.css';

const ReportsPage = () => {
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
        const { dateFrom, dateTo, company } = reportParams;

        if (!dateFrom || !dateTo) {
            alert("Wybierz datę początkową i końcową!");
            return;
        }

        try {
            const token = localStorage.getItem("token");
            const response = await fetch(
                `http://localhost:8081/api/reports/download?format=${format}&dateFrom=${dateFrom}&dateTo=${dateTo}&company=${company}`,
                {
                    method: 'GET',
                    headers: {
                        'Authorization': `Bearer ${token}`
                    }
                }
            );

            if (!response.ok) throw new Error("Błąd pobierania raportu");

            const blob = await response.blob();
            const url = window.URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.href = url;
            a.download = `raport_${dateFrom}_${dateTo}.${format === 'excel' ? 'xlsx' : 'pdf'}`;
            document.body.appendChild(a);
            a.click();
            window.URL.revokeObjectURL(url);
            a.remove();
        } catch (error) {
            console.error("Błąd:", error);
            alert("Nie udało się wygenerować raportu.");
        }
    };

    return (
        <div className={styles.wrapper}>
            <Navbar isSidebarOpen={isSidebarOpen} onToggleSidebar={toggleSidebar} />

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