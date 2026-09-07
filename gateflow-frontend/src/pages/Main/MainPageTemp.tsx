import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import Navbar from "../../components/Common/Navbar";
import styles from './MainPage.module.css';
import { apiFetch } from "../../api";

interface VisitDto {
    id: number;
    registrationNumber: string;
    driverName?: string;
    surname?: string;
    companyName?: string;
    entryTime: string;
    exitTime?: string;
    entryDate?: string;
    entryCargo?: string;
    exitCargo?: string;
    durationMinutes: number;
    status: string;
}

const MainPage = () => {
    const navigate = useNavigate();
    const [isSidebarOpen, setIsSidebarOpen] = useState(false);
    const [entries, setEntries] = useState<VisitDto[]>([]);
    const [exits, setExits] = useState<VisitDto[]>([]);
    const [selectedEntryId, setSelectedEntryId] = useState<number | null>(null);

    const toggleSidebar = () => setIsSidebarOpen(!isSidebarOpen);

    useEffect(() => {
        fetchData();
    }, []);

    const fetchData = async () => {
        try {
            const [onSiteRes, allRes] = await Promise.all([
                apiFetch('/api/visits/on-site'),
                apiFetch('/api/visits')
            ]);

            if (!onSiteRes.ok || !allRes.ok) {
                if (onSiteRes.status === 403) {
                    localStorage.removeItem("token");
                    navigate("/login");
                }
                return;
            }

            const onSiteData = await onSiteRes.json();
            const allData = await allRes.json();

            const activeEntries = Array.isArray(onSiteData) 
                ? onSiteData 
                : (onSiteData._embedded?.visits || []);
            setEntries([...activeEntries].reverse());

            const allEntries = Array.isArray(allData) 
                ? allData 
                : (allData._embedded?.visits || []);
            const finished = allEntries
                .filter((v: VisitDto) => v.exitTime !== null && v.exitTime !== undefined)
                .sort((a: VisitDto, b: VisitDto) => 
                    new Date(b.exitTime!).getTime() - new Date(a.exitTime!).getTime()
                );
            setExits(finished);
            
        } catch (error) {
            console.error("Błąd pobierania danych:", error);
        }
    };

    const handleEndStay = async () => {
        if (selectedEntryId === null) return;
        try {
            await apiFetch(`/api/visits/${selectedEntryId}/exit`, { method: 'PUT' });
            setSelectedEntryId(null);
            fetchData(); 
        } catch (error) {
            alert("Nie udało się zakończyć pobytu");
        }
    };

    const getDriverDisplayName = (v: VisitDto) => {
        const parts = [v.driverName, v.surname].filter(Boolean);
        return parts.length > 0 ? parts.join(" ") : "-";
    };

    return (
        <div className={styles.wrapper}>
            <Navbar isSidebarOpen={isSidebarOpen} onToggleSidebar={toggleSidebar} />

            <main className={`${styles.content} ${isSidebarOpen ? styles.contentShifted : ''}`}>
                <div className={styles.actionCenter}>
                    <button className={styles.addEntryBtn} onClick={() => navigate('/add-entry')}>
                        + Dodaj Wjazd
                    </button>
                </div>

                <div className={styles.listsContainer}>
                    <section className={styles.listSection}>
                        <div className={styles.sectionHeader}>
                            <h2>Wjazdy pojazdów</h2>
                            <button className={styles.endStayBtn} onClick={handleEndStay} disabled={selectedEntryId === null}>
                                Zakończ pobyt
                            </button>
                        </div>
                        <table className={styles.vehicleTable}>
                            <thead>
                                <tr><th>Nr Rej</th><th>Kierowca</th><th>Ładunek</th><th>Firma</th><th>Wjazd</th></tr>
                            </thead>
                            <tbody>
                                {entries.map(v => (
                                    <tr key={v.id} 
                                        className={selectedEntryId === v.id ? styles.selectedRow : ''} 
                                        onClick={() => setSelectedEntryId(v.id)}>
                                        <td><strong>{v.registrationNumber}</strong></td>
                                        <td>{getDriverDisplayName(v)}</td>
                                        <td>{v.entryCargo || '-'}</td>
                                        <td>{v.companyName || '-'}</td>
                                        <td>{v.entryTime ? new Date(v.entryTime).toLocaleTimeString() : '-'}</td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </section>

                    <section className={styles.listSection}>
                        <div className={styles.sectionHeader}><h2>Wyjazdy</h2></div>
                        <table className={styles.vehicleTable}>
                            <thead>
                                <tr><th>Nr Rej</th><th>Kierowca</th><th>Wyjazd</th></tr>
                            </thead>
                            <tbody>
                                {exits.map(v => (
                                    <tr key={v.id}>
                                        <td><strong>{v.registrationNumber}</strong></td>
                                        <td>{getDriverDisplayName(v)}</td>
                                        <td>{v.exitTime ? new Date(v.exitTime).toLocaleTimeString() : '-'}</td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </section>
                </div>
            </main>
        </div>
    );
};

export default MainPage;