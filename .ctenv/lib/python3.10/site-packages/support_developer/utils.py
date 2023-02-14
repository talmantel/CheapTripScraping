import traceback


def is_directly_imported_in_jupyter_notebook() -> bool:
    """Returns whether the current stack starts from a direct import in a Jupyter Notebook.
    
    Implementative details
    -------------------------
    To check whether we are in a Jupyter Notebook and the root package is being
    directly imported from within a Jupyter Notebook, we use the following heuristic.

    1. The number of `__init__.py` documents in the stack trace must be exactly one
    2. There must be a method called `get_ipython` available, without being imported
    3. The shell must be `ZMQInteractiveShell`

    """
    stack_trace = traceback.extract_stack()[:-1]

    number_of_inits = sum([
        trace.filename.endswith("__init__.py")
        for trace in stack_trace
    ])

    if number_of_inits != 1:
        return False

    try:
        return get_ipython().__class__.__name__ == 'ZMQInteractiveShell'
    except NameError:
        return False
