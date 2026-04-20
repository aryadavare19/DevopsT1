import { useState, useEffect, useCallback } from "react";

const API = "https://devopst1.onrender.com/api";

const SKILLS = ["plumber", "electrician", "carpenter", "painter"];

const AREAS  = ["Pune", "Mumbai", "Nashik"];

const STATUS_META = {
  OPEN:      { color: "#d97706", bg: "#fef3c7" },
  ASSIGNED:  { color: "#2563eb", bg: "#dbeafe" },
  COMPLETED: { color: "#16a34a", bg: "#dcfce7" },
  SCHEDULED: { color: "#7c3aed", bg: "#ede9fe" },
};

const SKILL_ICON = {
  plumber:     "🔧",
  electrician: "⚡",
  carpenter:   "🪚",
  painter:     "🖌️",
};

async function api(path, opts = {}) {
  try {
    const res = await fetch(API + path, {
      headers: { "Content-Type": "application/json" },
      ...opts,
    });

    if (!res.ok) {
      const text = await res.text();
      throw new Error(text || "Server error");
    }

    return res.json();
  } catch (e) {
    throw new Error("Server waking up... try again in 5 seconds");
  }
}

/* ── Primitives ─────────────────────────────────────────────── */

const Badge = ({ label, meta }) => {
  const m = meta || STATUS_META[label?.toUpperCase()] || { color: "#6b7280", bg: "#f3f4f6" };
  return (
    <span style={{
      background: m.bg, color: m.color, border: `1px solid ${m.color}33`,
      padding: "3px 10px", borderRadius: 99, fontSize: 11,
      fontWeight: 700, letterSpacing: ".05em", textTransform: "uppercase",
      whiteSpace: "nowrap",
    }}>{label}</span>
  );
};

const Stars = ({ value }) => (
  <span style={{ fontSize: 13 }}>
    {[1,2,3,4,5].map(i => (
      <span key={i} style={{ color: i <= Math.round(value) ? "#f59e0b" : "#d1d5db" }}>★</span>
    ))}
    <span style={{ color: "#9ca3af", fontSize: 12, marginLeft: 4 }}>{value ? value.toFixed(1) : "0.0"}</span>
  </span>
);

const Field = ({ label, children, style: s }) => (
  <label style={{ display: "flex", flexDirection: "column", gap: 5, ...s }}>
    <span style={{ fontSize: 11, fontWeight: 700, color: "#6b7280", letterSpacing: ".08em", textTransform: "uppercase" }}>{label}</span>
    {children}
  </label>
);

const inputStyle = {
  background: "#fff", border: "1.5px solid #e5e7eb", borderRadius: 8,
  color: "#111827", padding: "9px 12px", fontSize: 14, outline: "none",
  fontFamily: "inherit", transition: "border .15s", width: "100%",
};

const Inp = ({ label, style: s, ...p }) => (
  <Field label={label} style={s}>
    <input style={inputStyle}
      onFocus={e => e.target.style.borderColor = "#6366f1"}
      onBlur={e => e.target.style.borderColor = "#e5e7eb"}
      {...p} />
  </Field>
);

const Sel = ({ label, options, style: s, ...p }) => (
  <Field label={label} style={s}>
    <select style={{ ...inputStyle, cursor: "pointer" }} {...p}>
      {options.map(o => <option key={o.value ?? o} value={o.value ?? o}>{o.label ?? o}</option>)}
    </select>
  </Field>
);

const Btn = ({ children, variant = "primary", loading, style: s, ...p }) => {
  const variants = {
    primary: { background: "linear-gradient(135deg,#6366f1,#4f46e5)", color: "#fff", border: "none" },
    secondary: { background: "#fff", color: "#4f46e5", border: "1.5px solid #c7d2fe" },
    success: { background: "linear-gradient(135deg,#16a34a,#15803d)", color: "#fff", border: "none" },
    danger: { background: "#fff", color: "#dc2626", border: "1.5px solid #fecaca" },
    ghost: { background: "transparent", color: "#6b7280", border: "1.5px solid #e5e7eb" },
  };
  return (
    <button style={{
      ...variants[variant], padding: "9px 20px", borderRadius: 8,
      fontFamily: "inherit", fontWeight: 700, fontSize: 13, cursor: p.disabled ? "not-allowed" :  "pointer",
      letterSpacing: ".03em", transition: "all .15s", opacity: p.disabled ? .6 : 1,
      display: "inline-flex", alignItems: "center", gap: 6, ...s,
    }} {...p}>
      {loading ? <span style={{ display: "inline-block", width: 14, height: 14, border: "2px solid currentColor", borderTopColor: "transparent", borderRadius: "50%", animation: "spin .7s linear infinite" }} /> : null}
      {children}
    </button>
  );
};

const Card = ({ children, style: s, hover }) => {
  const [h, setH] = useState(false);
  return (
    <div
      onMouseEnter={() => hover && setH(true)}
      onMouseLeave={() => hover && setH(false)}
      style={{
        background: "#fff", border: "1.5px solid #e5e7eb", borderRadius: 14,
        padding: "20px 22px", transition: "box-shadow .2s, transform .2s",
        boxShadow: h ? "0 8px 30px #6366f115" : "0 1px 3px #0000000a",
        transform: h ? "translateY(-2px)" : "none", ...s,
      }}>{children}</div>
  );
};

const Modal = ({ children, onClose }) => (
  <div onClick={onClose} style={{
    position: "fixed", inset: 0, background: "#00000055", backdropFilter: "blur(4px)",
    display: "flex", alignItems: "center", justifyContent: "center", zIndex: 99, padding: 20,
  }}>
    <div onClick={e => e.stopPropagation()} style={{
      background: "#fff", borderRadius: 16, padding: 28, width: "100%", maxWidth: 420,
      boxShadow: "0 20px 60px #0000002a",
    }}>{children}</div>
  </div>
);

const Toast = ({ msg, type, onClose }) => msg ? (
  <div style={{
    position: "fixed", bottom: 24, right: 24, zIndex: 999,
    background: type === "error" ? "#fef2f2" : "#f0fdf4",
    border: `1.5px solid ${type === "error" ? "#fecaca" : "#bbf7d0"}`,
    color: type === "error" ? "#dc2626" : "#16a34a",
    padding: "12px 20px", borderRadius: 10, fontSize: 14, fontWeight: 600,
    boxShadow: "0 4px 20px #0000001a", display: "flex", gap: 12, alignItems: "center",
    animation: "slideUp .25s ease",
  }}>
    {type === "error" ? "⚠️" : "✅"} {msg}
    <button onClick={onClose} style={{ background: "none", border: "none", cursor: "pointer", color: "inherit", fontSize: 18, lineHeight: 1 }}>×</button>
  </div>
) : null;

const Divider = ({ label }) => (
  <div style={{ display: "flex", alignItems: "center", gap: 12, margin: "4px 0" }}>
    <span style={{ flex: 1, height: 1, background: "#e5e7eb" }} />
    {label && <span style={{ fontSize: 11, color: "#9ca3af", fontWeight: 600, letterSpacing: ".08em", textTransform: "uppercase" }}>{label}</span>}
    <span style={{ flex: 1, height: 1, background: "#e5e7eb" }} />
  </div>
);

/* ── Find Workers ───────────────────────────────────────────── */
function FindWorkers({ toast }) {
  const [skill, setSkill] = useState("");
  const [area, setArea]   = useState("");
  const [workers, setWorkers] = useState(null);
  const [loading, setLoading] = useState(false);

  const search = async (all = false) => {
    setLoading(true);
    try {
      const data = all
        ? await api("/workers/all")
        : await api(`/workers?${new URLSearchParams({ ...(skill && { skill }), ...(area && { area }) })}`);
      setWorkers(data);
      if (!data.length) toast("No workers found for that filter.", "info");
    } catch { toast("Cannot reach server. Is it running?", "error"); setWorkers([]); }
    setLoading(false);
  };

  return (
    <div style={{ display: "flex", flexDirection: "column", gap: 20 }}>
      <Card>
        <div style={{ display: "flex", gap: 12, flexWrap: "wrap", alignItems: "flex-end" }}>
          <Sel label="Skill" value={skill} onChange={e => setSkill(e.target.value)} style={{ flex: 1, minWidth: 140 }}
            options={[{ value: "", label: "Any skill" }, ...SKILLS.map(s => ({ value: s, label: `${SKILL_ICON[s]} ${s.charAt(0).toUpperCase()+s.slice(1)}` }))]} />
          <Sel label="Area" value={area} onChange={e => setArea(e.target.value)} style={{ flex: 1, minWidth: 120 }}
            options={[{ value: "", label: "Any area" }, ...AREAS]} />
          <Btn onClick={() => search(false)} loading={loading} style={{ alignSelf: "flex-end" }}>Search</Btn>
          <Btn variant="secondary" onClick={() => search(true)} loading={loading} style={{ alignSelf: "flex-end" }}>All Workers</Btn>
        </div>
      </Card>

      {workers !== null && (
        <div style={{ display: "grid", gridTemplateColumns: "repeat(auto-fill,minmax(290px,1fr))", gap: 14 }}>
          {workers.map(w => (
            <Card key={w.id} hover style={{ display: "flex", flexDirection: "column", gap: 12 }}>
              <div style={{ display: "flex", justifyContent: "space-between", alignItems: "flex-start" }}>
                <div style={{ display: "flex", gap: 12, alignItems: "center" }}>
                  <div style={{
                    width: 44, height: 44, borderRadius: 12, background: "#eef2ff",
                    display: "flex", alignItems: "center", justifyContent: "center", fontSize: 20,
                  }}>{SKILL_ICON[w.skill] || "👷"}</div>
                  <div>
                    <div style={{ fontWeight: 700, fontSize: 15, color: "#111827" }}>{w.name}</div>
                    <div style={{ fontSize: 12, color: "#9ca3af", marginTop: 1 }}>{w.id}</div>
                  </div>
                </div>
                <Badge label={w.available ? "Available" : "Busy"}
                  meta={w.available ? { color: "#16a34a", bg: "#dcfce7" } : { color: "#dc2626", bg: "#fee2e2" }} />
              </div>
              <Divider />
              <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
                <div style={{ display: "flex", gap: 6 }}>
                  <Badge label={w.skill} meta={{ color: "#6366f1", bg: "#eef2ff" }} />
                  <Badge label={w.area} meta={{ color: "#0891b2", bg: "#e0f2fe" }} />
                </div>
                <Stars value={w.rating} />
              </div>
              <div style={{ fontSize: 13, color: "#6b7280", display: "flex", alignItems: "center", gap: 6 }}>
                <span>📞</span> {w.phone}
              </div>
            </Card>
          ))}
        </div>
      )}

      {workers?.length === 0 && (
        <div style={{ textAlign: "center", padding: "60px 0", color: "#9ca3af" }}>
          <div style={{ fontSize: 40, marginBottom: 12 }}>🔍</div>
          <div style={{ fontWeight: 600 }}>No workers found</div>
          <div style={{ fontSize: 13, marginTop: 4 }}>Try different filters</div>
        </div>
      )}
    </div>
  );
}

/* ── Post Job ───────────────────────────────────────────────── */
function PostJob({ toast }) {
  const [form, setForm] = useState({ customerId: "CUS001", skill: "plumber", area: "Pune", description: "" });
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);

  const set = k => e => setForm(f => ({ ...f, [k]: e.target.value }));

  const submit = async () => {
    if (!form.description.trim()) { toast("Please add a description.", "error"); return; }
    setLoading(true);
    try {
      const job = await api("/jobs", { method: "POST", body: JSON.stringify(form) });
      setResult(job);
      toast(`Job ${job.jobId} posted successfully!`, "success");
      setForm(f => ({ ...f, description: "" }));
    } catch (e) { toast(e.message, "error"); }
    setLoading(false);
  };

  return (
    <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: 20, alignItems: "start" }}>
      <Card style={{ display: "flex", flexDirection: "column", gap: 16 }}>
        <h3 style={{ margin: 0, fontSize: 16, fontWeight: 700, color: "#111827" }}>New Job Request</h3>
        <Divider />
        <Inp label="Customer ID" value={form.customerId} onChange={set("customerId")} placeholder="e.g. CUS001" />
        <div style={{ display: "flex", gap: 12 }}>
          <Sel label="Skill Needed" value={form.skill} onChange={set("skill")} style={{ flex: 1 }}
            options={SKILLS.map(s => ({ value: s, label: `${SKILL_ICON[s]} ${s.charAt(0).toUpperCase()+s.slice(1)}` }))} />
          <Sel label="Area" value={form.area} onChange={set("area")} style={{ flex: 1 }} options={AREAS} />
        </div>
        <Field label="Description">
          <textarea value={form.description} onChange={set("description")}
            placeholder="Describe the work needed in detail…"
            style={{ ...inputStyle, minHeight: 100, resize: "vertical" }}
            onFocus={e => e.target.style.borderColor = "#6366f1"}
            onBlur={e => e.target.style.borderColor = "#e5e7eb"} />
        </Field>
        <Btn onClick={submit} loading={loading} style={{ alignSelf: "flex-start" }}>Post Job</Btn>
      </Card>

      <div style={{ display: "flex", flexDirection: "column", gap: 14 }}>
        {result && (
          <Card style={{ borderColor: "#c7d2fe", background: "#fafafe" }}>
            <div style={{ fontSize: 11, fontWeight: 800, color: "#6366f1", letterSpacing: ".1em", marginBottom: 14 }}>✅ JOB CREATED</div>
            <div style={{ display: "flex", flexDirection: "column", gap: 10 }}>
              {[["Job ID", result.jobId], ["Skill", result.skill], ["Area", result.area], ["Customer", result.customerId]].map(([k, v]) => (
                <div key={k} style={{ display: "flex", justifyContent: "space-between", fontSize: 14 }}>
                  <span style={{ color: "#6b7280" }}>{k}</span>
                  <span style={{ fontWeight: 600, color: "#111827" }}>{v}</span>
                </div>
              ))}
              <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", fontSize: 14 }}>
                <span style={{ color: "#6b7280" }}>Status</span>
                <Badge label={result.status} />
              </div>
            </div>
          </Card>
        )}

        <Card style={{ background: "#f9fafb" }}>
          <div style={{ fontSize: 13, fontWeight: 700, color: "#374151", marginBottom: 10 }}>💡 Tips</div>
          <ul style={{ margin: 0, padding: "0 0 0 16px", display: "flex", flexDirection: "column", gap: 6 }}>
            {["Be specific about the problem", "Mention preferred time if any", "Include access instructions if needed"].map(t => (
              <li key={t} style={{ fontSize: 13, color: "#6b7280" }}>{t}</li>
            ))}
          </ul>
        </Card>
      </div>
    </div>
  );
}

/* ── Open Jobs ──────────────────────────────────────────────── */
function OpenJobs({ toast }) {
  const [jobs, setJobs]     = useState(null);
  const [loading, setLoading] = useState(false);
  const [assign, setAssign] = useState(null);
  const [aForm, setAForm]   = useState({ workerId: "W001", customerId: "CUS001", date: "" });
  const [aLoading, setALoading] = useState(false);

  const load = useCallback(async () => {
    setLoading(true);
    try { setJobs(await api("/jobs/open")); }
    catch { toast("Server error", "error"); setJobs([]); }
    setLoading(false);
  }, []);

  useEffect(() => { load(); }, [load]);

  const doAssign = async () => {
    if (!aForm.date) { toast("Please pick a date", "error"); return; }
    setALoading(true);
    try {
      const b = await api("/jobs/assign", { method: "POST", body: JSON.stringify({ jobId: assign.jobId, ...aForm }) });
      toast(`Booking ${b.bookingId} created!`, "success");
      setAssign(null); load();
    } catch (e) { toast(e.message, "error"); }
    setALoading(false);
  };

  return (
    <div style={{ display: "flex", flexDirection: "column", gap: 16 }}>
      <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
        <div style={{ fontSize: 13, color: "#6b7280" }}>
          {jobs ? `${jobs.length} open job${jobs.length !== 1 ? "s" : ""}` : "Loading…"}
        </div>
        <Btn variant="ghost" onClick={load} loading={loading}>↻ Refresh</Btn>
      </div>

      {jobs?.length === 0 && (
        <div style={{ textAlign: "center", padding: "60px 0", color: "#9ca3af" }}>
          <div style={{ fontSize: 40, marginBottom: 12 }}>📋</div>
          <div style={{ fontWeight: 600 }}>No open jobs</div>
        </div>
      )}

      <div style={{ display: "flex", flexDirection: "column", gap: 10 }}>
        {jobs?.map(j => (
          <Card key={j.jobId} hover style={{ display: "flex", flexWrap: "wrap", gap: 16, alignItems: "center" }}>
            <div style={{ width: 42, height: 42, borderRadius: 10, background: "#fef3c7", display: "flex", alignItems: "center", justifyContent: "center", fontSize: 20, flexShrink: 0 }}>
              {SKILL_ICON[j.skill] || "📋"}
            </div>
            <div style={{ flex: 1, minWidth: 180 }}>
              <div style={{ fontWeight: 700, fontSize: 15, color: "#111827" }}>{j.jobId}</div>
              <div style={{ fontSize: 13, color: "#6b7280", marginTop: 3 }}>{j.description || "No description provided"}</div>
            </div>
            <div style={{ display: "flex", gap: 8, flexWrap: "wrap", alignItems: "center" }}>
              <Badge label={j.skill} meta={{ color: "#6366f1", bg: "#eef2ff" }} />
              <Badge label={j.area} meta={{ color: "#0891b2", bg: "#e0f2fe" }} />
              <Badge label={j.status} />
            </div>
            <Btn variant="secondary" style={{ fontSize: 12 }} onClick={() => setAssign(j)}>Assign Worker →</Btn>
          </Card>
        ))}
      </div>

      {assign && (
        <Modal onClose={() => setAssign(null)}>
          <div style={{ fontWeight: 800, fontSize: 18, color: "#111827", marginBottom: 4 }}>Assign Worker</div>
          <div style={{ fontSize: 13, color: "#6b7280", marginBottom: 20 }}>
            {assign.jobId} · {SKILL_ICON[assign.skill]} {assign.skill} · {assign.area}
          </div>
          <div style={{ display: "flex", flexDirection: "column", gap: 14 }}>
            <Inp label="Worker ID" value={aForm.workerId} onChange={e => setAForm(f => ({ ...f, workerId: e.target.value }))} placeholder="e.g. W001" />
            <Inp label="Customer ID" value={aForm.customerId} onChange={e => setAForm(f => ({ ...f, customerId: e.target.value }))} />
            <Inp label="Scheduled Date" type="date" value={aForm.date} onChange={e => setAForm(f => ({ ...f, date: e.target.value }))} />
            <div style={{ display: "flex", gap: 10, marginTop: 4 }}>
              <Btn onClick={doAssign} loading={aLoading}>Confirm Booking</Btn>
              <Btn variant="ghost" onClick={() => setAssign(null)}>Cancel</Btn>
            </div>
          </div>
        </Modal>
      )}
    </div>
  );
}

/* ── Bookings ───────────────────────────────────────────────── */
function Bookings({ toast }) {
  const [mode, setMode]       = useState("customer");
  const [id, setId]           = useState("CUS001");
  const [bookings, setBookings] = useState(null);
  const [loading, setLoading]  = useState(false);
  const [completeId, setCompleteId] = useState("");
  const [rateForm, setRateForm]     = useState({ workerId: "", rating: "5" });

  const load = async () => {
    setLoading(true);
    try {
      const path = mode === "customer" ? `/jobs/bookings/customer?customerId=${id}` : `/jobs/bookings/worker?workerId=${id}`;
      setBookings(await api(path));
    } catch { toast("Server error", "error"); setBookings([]); }
    setLoading(false);
  };

  const complete = async () => {
    if (!completeId) { toast("Enter a Booking ID", "error"); return; }
    try {
      const r = await api("/jobs/complete", { method: "POST", body: JSON.stringify({ bookingId: completeId }) });
      toast(r.success ? "Job marked complete!" : r.message, r.success ? "success" : "error");
      setCompleteId(""); load();
    } catch (e) { toast(e.message, "error"); }
  };

  const rate = async () => {
    if (!rateForm.workerId) { toast("Enter Worker ID", "error"); return; }
    try {
      const r = await api("/jobs/rate", { method: "POST", body: JSON.stringify(rateForm) });
      toast(`New rating: ${r.newRating} ⭐`, "success");
    } catch (e) { toast(e.message, "error"); }
  };

  return (
    <div style={{ display: "flex", flexDirection: "column", gap: 20 }}>
      <Card style={{ display: "flex", flexDirection: "column", gap: 14 }}>
        <div style={{ display: "flex", gap: 8 }}>
          {["customer", "worker"].map(m => (
            <Btn key={m} variant={mode === m ? "primary" : "ghost"}
              style={{ textTransform: "capitalize" }}
              onClick={() => { setMode(m); setId(m === "customer" ? "CUS001" : "W001"); setBookings(null); }}>
              {m === "customer" ? "👤" : "🔧"} {m}
            </Btn>
          ))}
        </div>
        <div style={{ display: "flex", gap: 12, alignItems: "flex-end" }}>
          <Inp label={mode === "customer" ? "Customer ID" : "Worker ID"} value={id} onChange={e => setId(e.target.value)} style={{ flex: 1 }} />
          <Btn onClick={load} loading={loading} style={{ alignSelf: "flex-end" }}>Fetch Bookings</Btn>
        </div>
      </Card>

      {bookings !== null && bookings.length === 0 && (
        <div style={{ textAlign: "center", padding: "40px 0", color: "#9ca3af" }}>
          <div style={{ fontSize: 36, marginBottom: 10 }}>🗓</div>
          <div style={{ fontWeight: 600 }}>No bookings found</div>
        </div>
      )}

      {bookings?.length > 0 && (
        <div style={{ display: "flex", flexDirection: "column", gap: 10 }}>
          {bookings.map(b => (
            <Card key={b.bookingId} hover style={{ display: "flex", flexWrap: "wrap", gap: 12, alignItems: "center" }}>
              <div style={{ flex: 1, minWidth: 160 }}>
                <div style={{ fontWeight: 700, color: "#111827", fontSize: 15 }}>{b.bookingId}</div>
                <div style={{ fontSize: 12, color: "#9ca3af", marginTop: 3, display: "flex", gap: 12 }}>
                  <span>Job: {b.jobId}</span>
                  <span>Worker: {b.workerId}</span>
                  <span>📅 {b.scheduledDate}</span>
                </div>
              </div>
              <Badge label={b.status} />
            </Card>
          ))}
        </div>
      )}

      <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: 14 }}>
        <Card style={{ display: "flex", flexDirection: "column", gap: 14 }}>
          <div style={{ fontWeight: 700, fontSize: 15, color: "#111827" }}>✅ Complete a Job</div>
          <Inp label="Booking ID" value={completeId} onChange={e => setCompleteId(e.target.value)} placeholder="e.g. BK1" />
          <Btn variant="success" onClick={complete}>Mark as Complete</Btn>
        </Card>
        <Card style={{ display: "flex", flexDirection: "column", gap: 14 }}>
          <div style={{ fontWeight: 700, fontSize: 15, color: "#111827" }}>⭐ Rate a Worker</div>
          <Inp label="Worker ID" value={rateForm.workerId} onChange={e => setRateForm(f => ({ ...f, workerId: e.target.value }))} placeholder="e.g. W001" />
          <Sel label="Rating" value={rateForm.rating} onChange={e => setRateForm(f => ({ ...f, rating: e.target.value }))}
            options={["5","4","3","2","1"].map(v => ({ value: v, label: "★".repeat(+v) + " — " + v + " star" + (v > 1 ? "s" : "") }))} />
          <Btn onClick={rate}>Submit Rating</Btn>
        </Card>
      </div>
    </div>
  );
}

/* ── Root ───────────────────────────────────────────────────── */
const TABS = [
  { id: "find",     label: "Find Workers", icon: "🔍" },
  { id: "post",     label: "Post Job",     icon: "📋" },
  { id: "jobs",     label: "Open Jobs",    icon: "📂" },
  { id: "bookings", label: "Bookings",     icon: "🗓" },
];

export default function App() {
  const [tab, setTab] = useState("find");
  const [toast, setToast] = useState({ msg: "", type: "success" });

  const showToast = (msg, type = "success") => {
    setToast({ msg, type });
    setTimeout(() => setToast({ msg: "", type }), 4000);
  };

  return (
    <>
      <style>{`
        @import url('https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700;800&display=swap');
        *, *::before, *::after { box-sizing: border-box; margin: 0; padding: 0; }
        body { background: #f3f4f6; color: #111827; font-family: 'Plus Jakarta Sans', sans-serif; min-height: 100vh; }
        select option { background: #fff; }
        @keyframes spin { to { transform: rotate(360deg); } }
        @keyframes slideUp { from { transform: translateY(16px); opacity: 0; } to { transform: translateY(0); opacity: 1; } }
        ::-webkit-scrollbar { width: 6px; }
        ::-webkit-scrollbar-track { background: #f3f4f6; }
        ::-webkit-scrollbar-thumb { background: #d1d5db; border-radius: 3px; }
      `}</style>

      {/* Sidebar */}
      <div style={{ display: "flex", minHeight: "100vh" }}>
        <aside style={{
          width: 220, background: "#fff", borderRight: "1.5px solid #e5e7eb",
          display: "flex", flexDirection: "column", padding: "24px 16px",
          position: "fixed", top: 0, left: 0, height: "100vh",
        }}>
          {/* Logo */}
          <div style={{ padding: "0 8px 24px", borderBottom: "1.5px solid #e5e7eb", marginBottom: 20 }}>
            <div style={{ fontSize: 20, fontWeight: 800, color: "#4f46e5", letterSpacing: "-.03em" }}>
              Worker<span style={{ color: "#111827" }}>Finder</span>
            </div>
            <div style={{ fontSize: 11, color: "#9ca3af", marginTop: 3, fontWeight: 500 }}> City </div>
          </div>

          {/* Nav */}
          <nav style={{ display: "flex", flexDirection: "column", gap: 4 }}>
            {TABS.map(t => (
              <button key={t.id} onClick={() => setTab(t.id)} style={{
                display: "flex", alignItems: "center", gap: 10,
                padding: "10px 12px", borderRadius: 10, border: "none", cursor: "pointer",
                fontFamily: "inherit", fontSize: 14, fontWeight: tab === t.id ? 700 : 500,
                background: tab === t.id ? "#eef2ff" : "transparent",
                color: tab === t.id ? "#4f46e5" : "#6b7280",
                transition: "all .15s", textAlign: "left",
              }}>
                <span style={{ fontSize: 16 }}>{t.icon}</span>
                {t.label}
              </button>
            ))}
          </nav>

          {/* Footer */}
          <div style={{ marginTop: "auto", padding: "16px 8px 0", borderTop: "1.5px solid #e5e7eb" }}>
            <div style={{ fontSize: 11, color: "#9ca3af", lineHeight: 1.6 }}>
              <div style={{ fontWeight: 600, color: "#6b7280", marginBottom: 4 }}>Stack</div>
              React · Spring Boot · RMI
            </div>
          </div>
        </aside>

        {/* Main content */}
        <main style={{ marginLeft: 220, flex: 1, padding: "32px 32px 60px" }}>
          {/* Page header */}
          <div style={{ marginBottom: 28 }}>
            <h1 style={{ fontSize: 26, fontWeight: 800, color: "#111827", letterSpacing: "-.03em" }}>
              {TABS.find(t => t.id === tab)?.icon} {TABS.find(t => t.id === tab)?.label}
            </h1>
            <p style={{ fontSize: 14, color: "#6b7280", marginTop: 4 }}>
              {tab === "find"     && "Search and browse available skilled workers"}
              {tab === "post"     && "Submit a new job request for a skilled worker"}
              {tab === "jobs"     && "View open jobs and assign workers to them"}
              {tab === "bookings" && "Track bookings, mark jobs complete, and rate workers"}
            </p>
          </div>

          {tab === "find"     && <FindWorkers toast={showToast} />}
          {tab === "post"     && <PostJob     toast={showToast} />}
          {tab === "jobs"     && <OpenJobs    toast={showToast} />}
          {tab === "bookings" && <Bookings    toast={showToast} />}
        </main>
      </div>

      <Toast msg={toast.msg} type={toast.type} onClose={() => setToast({ msg: "" })} />
    </>
  );
}
