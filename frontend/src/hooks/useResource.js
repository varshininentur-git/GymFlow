import { useEffect, useState } from 'react';

export function useResource(load, dependencies = []) {
  const [data, setData] = useState(null);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(true);
  useEffect(() => {
    let current = true;
    setLoading(true);
    load().then(value => { if (current) setData(value); })
      .catch(problem => { if (current) setError(problem.message || 'Could not load data.'); })
      .finally(() => { if (current) setLoading(false); });
    return () => { current = false; };
  // Load again only when the caller's dependency values change.
  // eslint-disable-next-line react-hooks/exhaustive-deps
  }, dependencies);
  return { data, error, loading };
}
