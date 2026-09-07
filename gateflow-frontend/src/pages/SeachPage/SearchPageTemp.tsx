import { useState } from "react";
import styles from './SearchPage.module.css'; 
import Navbar from "../../components/Common/Navbar";
import { apiFetch } from "../../api";

interface SearchParams {
    reg: string; 
    name: string; 
    surname: string;
    company: string; 
    brand: string;
    entryDate: string;
}

interface VisitDto {
    id: number;
    registrationNumber: string | null;
    driverName: string | null;
    surname: string | null;
    companyName: string | null;
    entryTime: string | null;
    exitTime: string | null;
    entryCargo: string | null;
    exitCargo: string | null;
    durationMinutes: number | null;
    status: string;
}

const SearchPage = () => {
    const [isSidebarOpen, setIsSidebarOpen] = useState(false);
    const [results, setResults] = useState<VisitDto[]>([]);
    const [params, setParams] = useState<SearchParams>({
        reg: '', name: '', surname: '', company: '', brand: '', entryDate: ''
    });

    const toggleSidebar = () => setIsSidebarOpen(!isSidebarOpen);

    const handleSearch = async () => {
        const activeParams = Object.fromEntries(
            Object.entries(params).filter(([_, value]) => value.trim() !== '')
        );

        const queryParams = new URLSearchParams(activeParams);

        try {
            const res = await apiFetch(`/api/visits/search?${queryParams.toString()}`);
            if (!res.ok) throw new Error(`Błąd: ${res.status}`);
            const data = await res.json();
            setResults(Array.isArray(data) ? data : []);
        } catch (error) {
            console.error("Błąd wyszukiwania:", error);
            setResults([]);
        }
    };

    return (
        <div className={styles.wrapper}>
            <Navbar isSidebarOpen={isSidebarOpen} onToggleSidebar={toggleSidebar} />

            <main className={`${styles.content} ${isSidebarOpen ? styles.contentShifted : ''}`}>
                <div className={styles.listSection}>
                    <h2>Wyszukiwanie wizyt</h2>
                    <div className={styles.searchForm}>
                        <div className={styles.formGroup}>
                            <label>Nr Rejestracyjny</label>
                            <input 
                                value={params.reg} 
                                onChange={e => setParams({...params, reg: e.target.value})} 
                            />
                        </div>
                        <div className={styles.formGroup}>
                            <label>Imię kierowcy</label>
                            <input 
                                value={params.name} 
                                onChange={e => setParams({...params, name: e.target.value})} 
                            />
                        </div>
                        <div className={styles.formGroup}>
                            <label>Nazwisko</label>
                            <input 
                                value={params.surname} 
                                onChange={e => setParams({...params, surname: e.target.value})} 
                            />
                        </div>
                        <div className={styles.formGroup}>
                            <label>Firma</label>
                            <input 
                                value={params.company} 
                                onChange={e => setParams({...params, company: e.target.value})} 
                            />
                        </div>
                        <div className={styles.formGroup}>
                            <label>Marka</label>
                            <input 
                                value={params.brand} 
                                onChange={e => setParams({...params, brand: e.target.value})} 
                            />
                        </div>
                        <div className={styles.formGroup}>
                            <label>Data wjazdu</label>
                            <input 
                                type="date" 
                                value={params.entryDate} 
                                onChange={e => setParams({...params, entryDate: e.target.value})} 
                            />
                        </div>
                        
                        <button className={styles.searchBtn} onClick={handleSearch}>Szukaj</button>
                    </div>

                    <table className={styles.vehicleTable}>
                        <thead>
                            <tr>
                                <th>Nr Rej</th>
                                <th>Imię</th>
                                <th>Nazwisko</th>
                                <th>Firma</th>
                                <th>Ładunek</th>
                                <th>Wjazd</th>
                                <th>Czas (min)</th>
                                <th>Status</th>
                            </tr>
                        </thead>
                        <tbody>
                            {results.length > 0 ? results.map((v) => (
                                <tr key={v.id}>
                                    <td>{v.registrationNumber || '-'}</td>
                                    <td>{v.driverName || '-'}</td>
                                    <td>{v.surname || '-'}</td>
                                    <td>{v.companyName || '-'}</td>
                                    <td>{v.entryCargo || '-'}</td>
                                    <td>{v.entryTime ? new Date(v.entryTime).toLocaleString('pl-PL') : '-'}</td>
                                    <td>{v.durationMinutes ?? '-'}</td>
                                    <td>{v.status || '-'}</td>
                                </tr>
                            )) : (
                                <tr>
                                    <td colSpan={8} style={{ textAlign: 'center' }}>Brak wyników</td>
                                </tr>
                            )}
                        </tbody>
                    </table>
                </div>
            </main>
        </div>
    );
};

export default SearchPage;