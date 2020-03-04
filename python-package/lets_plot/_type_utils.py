#
# Copyright (c) 2019. JetBrains s.r.o.
# Use of this source code is governed by the MIT license that can be found in the LICENSE file.
#
import json
from abc import abstractmethod
from datetime import datetime

from typing import Dict

try:
    import numpy
except ImportError:
    numpy = None

try:
    import pandas
except ImportError:
    pandas = None

try:
    import shapely
except ImportError:
    shapely = None


# Parameter 'value' can also be pandas.DataFrame
def standardize_dict(value: Dict) -> Dict:
    result = {}
    for k, v in value.items():
        result[_standardize_value(k)] = _standardize_value(v)

    return result


def is_dict_or_dataframe(v):
    return isinstance(v, dict) or (pandas and isinstance(v, pandas.DataFrame))


def is_int(v):
    return isinstance(v, int) or (numpy and isinstance(v, numpy.integer))


def is_float(v):
    return isinstance(v, float) or (numpy and isinstance(v, numpy.floating))


def is_number(v):
    return is_int(v) or is_float(v)

def _standardize_value(v):
    if v is None:
        return v
    if isinstance(v, bool):
        return bool(v)
    if is_int(v):
        return int(v)
    if isinstance(v, str):
        return str(v)
    if is_float(v):
        return float(v)
    if is_dict_or_dataframe(v):
        return standardize_dict(v)
    if isinstance(v, list):
        return [_standardize_value(elem) for elem in v]
    if isinstance(v, tuple):
        return tuple(_standardize_value(elem) for elem in v)
    if (numpy and isinstance(v, numpy.ndarray)) or (pandas and isinstance(v, pandas.Series)):
        return _standardize_value(v.tolist())
    if isinstance(v, datetime):
        if (pandas and v is pandas.NaT):
            return None
        else:
            return v.timestamp() * 1000  # convert from second to millisecond
    if isinstance(v, CanToDataFrame):
        return standardize_dict(v.to_data_frame())
    if (shapely and isinstance(v, shapely.geometry.base.BaseGeometry)):
        return json.dumps(shapely.geometry.mapping(v))
    try:
        return repr(v)
    except Exception:
        raise Exception('Unsupported type: {0}({1})'.format(v, type(v)))


class CanToDataFrame:
    @abstractmethod
    def to_data_frame(self):  # -> pandas.DataFrame
        pass
