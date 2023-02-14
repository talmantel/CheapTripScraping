from typing import Optional
from .support_message import support_message


def support_luca(
    package_name: str,
    repository_name: Optional[str] = None,
):
    support_message(
        package_name=package_name,
        developer_name="Luca",
        github_handle="LucaCappelletti94",
        repository_name=repository_name,
        image_url="https://avatars.githubusercontent.com/u/7738570?v=4",
        number_of_imports=[3, 5, 10, 20, 50, 100, 500, 1000]
    )